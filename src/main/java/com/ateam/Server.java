package com.ateam;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/*
 * This class is the server side of the application. It manages multiple connections, waiting for messages to arrive.
 * When one client has sent a new message, it broadcast it to all current clients.
 */
/**
 *
 * @author pferna12
 */
public class Server {
    private static final Logger LOGGER = Logger.getLogger("Server");
    final int PORT = 49080;
    private UserAuth userAuth;
    private UserDB db = new UserDB();

    ArrayList<ChatRoom> rooms = new ArrayList<>();
    Socket socket = null;
    ServerSocket ss;
    Badwords bw = new Badwords();

    Runnable awaitNewConnections = () -> {
        try {
            LOGGER.info("[Server]\tWaiting for new connections");
            socket = ss.accept();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "[Server]\tError waiting for connections", e);
        }
    };
    Thread awaitNewConnectionsThread = new Thread(awaitNewConnections, "Accept socket");

    /**
     * @throws IOException
     * @throws FileNotFoundException
     *
     */
    public Server(){
        try {
            ss = new ServerSocket(PORT);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "[Server]\tThe server socket could not be initialized.", e);
        }

        this.userAuth = new UserAuth(db);
        this.rooms.add(new ChatRoom("general"));
        bw.loadBw();
    }

    /**
     * Entrypoint of the server.
     *
     * @throws Exception
     */
    public void run() throws Exception {
        LOGGER.info("[Server]\tTCP Server is starting up, listening at port " + PORT + ".");

        while (true) {
            // Setup new connections
            if (socket == null) {
                if (!awaitNewConnectionsThread.isAlive()) {
                    awaitNewConnectionsThread.start();
                }
            } else {
                addNewClientHandler(socket, userAuth);
                awaitNewConnectionsThread = new Thread(awaitNewConnections, "Accept socket");
            }

            disconnectOfflineClients();

            var clientsWithCommands = new ArrayList<ClientHandler>();

            for (var room: rooms) {
                clientsWithCommands.addAll(room.getUsers().stream()
                    .filter(c -> c.checkPendingMessages() && Command.parseCommand(c.peekLastMessage()) != Command.NOOP)
                    .toList()
                );
            }

            Iterator<ClientHandler> it = clientsWithCommands.iterator();

            while (it.hasNext()) {
                var client = it.next();

                var lastMessage = client.getLastMessage();
                var command = Command.parseCommand(lastMessage);

                switch (command) {
                    case JOIN -> {
                        var parseMsg = Arrays.asList(lastMessage.toLowerCase().split(" "));
                        var room = "";
                        if (parseMsg.size() > 1) {
                            room = parseMsg.get(1);
                            joinRoom(room, client);
                        } else {
                            client.sendMessage("Please specify a room to join (e.g. /join general");
                        }
                    }
                    case LEAVE -> leaveRoom(client);
                    case LIST -> listRoom(client);
                }
            }


            var clientsWithMessages = new ArrayList<ClientHandler>();
            for (var room: rooms) {
                clientsWithMessages.addAll(room.getUsers().stream()
                    .filter(c -> c.checkPendingMessages() && Command.parseCommand(c.peekLastMessage()) == Command.NOOP)
                    .toList()
                );
            }

            for (var client: clientsWithMessages) {
                broadcast(client);
            }

            Thread.sleep(1000);
        }
    }

    /**
     * Send a message to all clients in the same room.
     *
     * @param client
     * @throws com.ateam.ClientHandlerException
     */
    public void broadcast(ClientHandler client) throws ClientHandlerException {
        if (client.timedout()) {
            client.sendMessage("Please don't send messages too quickly.");
            client.getMessages();
            return ;
        }

        LOGGER.info("[Server]\t Broadcasting messages");

        var messages = client.getMessages();
        var room = getRoom(client);

        while (!messages.isEmpty()) {
            var message = filterBadword(messages.pop(), client);

            List<ClientHandler> otherClients = room.getUsers().stream()
                .filter(c -> c != client)
                .toList();

            for (var otherClient : otherClients) {
                LOGGER.info(
                        "[Server]\t Sending message \"" + message + "\" to client "
                                + client.socket.getInetAddress());
                otherClient.sendMessage("[" + client.getUsername() + "] " + message);
            }
        }
    }

    /**
     *
     * @param socket
     * @param ua
     * @throws ClientHandlerException
     */
    public void addNewClientHandler(Socket socket, UserAuth ua) throws ClientHandlerException {
        ClientHandler client = new ClientHandler(socket, ua);
        this.socket = null;

        for (var room : rooms) {
            if (room.getRoomName().equals("general")) {
                room.add(client);
            }
        }

        client.start();
        LOGGER.info("[Server]\tNew client connected");
    }

    /**
     * Check in which room is a particular client
     *
     * @param client
     * @return the room. {@code null} if the client could not be found.
     */
    public ChatRoom getRoom(ClientHandler client) {
        for (var room : rooms) {
            if (room.contains(client)) {
                return room;
            }
        }

        return null;
    }

    /**
     * Move the client to a room selected, if not exists, create one.
     *
     * @param roomname
     * @param client
     */
    public void joinRoom(String roomname, ClientHandler client) {
        getRoom(client).remove(client);

        var room = this.rooms.stream()
                .filter(r -> r.getRoomName().equals(roomname))
                .findFirst();

        if (room.isPresent()) {
            room.get().add(client);
            client.sendMessage("You have joined " + room.get().getRoomName()
                + ". There are " + room.get().getUsers().size() + " users online."
            );
        } else {
            var newRoom = new ChatRoom(roomname);
            newRoom.add(client);
            this.rooms.add(newRoom);
            client.sendMessage("You have joined " + newRoom.getRoomName()
                + ". There are currently no more users online."
            );
        }
    }

    /**
     * List all the rooms available to a client
     *
     * @param client
     */
    public void listRoom(ClientHandler client) {
        String chatslist = "Rooms avaliable: ";

        for (var room : rooms) {
            chatslist += room.getRoomName() + "(" + room.size() + ") ";
        }

        client.sendMessage(chatslist);
        LOGGER.info("[Server]\tList of rooms sent to client " + client.socket.getInetAddress());
    }

    /**
     * Move the client from the current room to "general".
     *
     * @param client ClientHandler to be moved to general.
     */
    public void leaveRoom(ClientHandler client) {
        joinRoom("general", client);
        client.sendMessage("You left the current room. You are now in general");
        LOGGER.info("[Server]\t" + client.getUsername() + " left the current room");
    }

    /**
     * Check how many users are currently in a room and remove it if it's empty
     */
    public void removeEmptyRooms() {
        var emptyRooms = this.rooms.stream()
                .filter(r -> r.size() == 0)
                .collect(Collectors.toList());

        this.rooms.removeAll(emptyRooms);
    }

    /**
     * Remove all offline clients from the server
     */
    public void disconnectOfflineClients() {
        for (var room : rooms) {
            var disconnected = room.getUsers().stream()
                    .filter(c -> !c.isConnected())
                    .toList();

            for (var c : disconnected) {
                room.remove(c);
            }
        }
    }

    public String filterBadword(String message, ClientHandler client){
        String[] words = message.split("\\W+");
        String filteredMessage = "";

        for (String word : words) {
            int wordLenght = word.length();

            if (bw.getBadwordsList().contains(word.toLowerCase())){
                word = word.substring(0, 1);

                for (int i = 1; i < wordLenght; i++) {
                    word += "♥";
                }

                client.warnBadWord();

                try {
                    db.writeDB(UserDB.DB_FILEPATH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            filteredMessage += word + " ";
        }
        return filteredMessage.trim();
    }
}
