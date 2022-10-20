package com.ateam;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    Socket socket = null;
    ServerSocket ss;
    private HashMap<String, ArrayList<ClientHandler>> chatRooms = new HashMap<>();

    Runnable awaitNewConnections = () -> {
        try {
            LOGGER.info("[Server]\tWaiting for new connections");
            socket = ss.accept();
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "[Server]\tError waiting for connections", e);
        }
    };
    Thread awaitNewConnectionsThread = new Thread(awaitNewConnections, "Accept socket");

    /**
     *
     */
    public Server() {
        try {
            ss = new ServerSocket(PORT);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "[Server]\tThe server socket could not be initialized.", e);
        }

        this.userAuth = new UserAuth(db);
        chatRooms.put("general", clients);
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

            for (var client: clients) {
                if (client.checkPendingMessages()) {
                    //LOGGER.info("[Server]\t Pending messages to be sent");

                    var lastMessage = client.getLastMessage();
                    var command = Command.parseCommand(lastMessage);

                    if (command != Command.NOOP) {

                        switch(command) {
                            case JOIN -> {
                                var parseMsg = Arrays.asList(lastMessage.toLowerCase().split(" "));
                                var room = "";
                                if (parseMsg.size() > 1) {
                                    room = parseMsg.get(1);
                                    joinRoom(room, client);
                                }
                            }
                            case LEAVE -> leaveRoom(client);
                            case LIST  -> listRoom(client);
                        }
                    }
                    else {
                        broadcast(client);
                    }
                }
            }

            Thread.sleep(1000);
        }
    }

    /**
     * Send a message to all clients
     *
     * @param client
     * @throws com.ateam.ClientHandlerException
     */
    // Broadcast should use the client instead of the messages
    public void broadcast(ClientHandler client) throws ClientHandlerException {
        LOGGER.info("[Server]\t Broadcasting messages");
        var messages = client.getMessages();

        while (!messages.isEmpty()) {
            var message = messages.pop();

            ArrayList<ClientHandler> otherClients = new ArrayList<>();

            for (var key: this.chatRooms.keySet()) {
                if (chatRooms.get(key).contains(client)) {
                    otherClients = this.chatRooms.get(key);
                }
            }

            for (var otherClient : otherClients) {
                LOGGER.info("[Server]\t Sending message \"" + message + "\" to client " + client.socket.getInetAddress());
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
        clients.add(client);
        client.start();
        LOGGER.info("[Server]\tNew client connected");
    }

    /**
     *
     * @return
     */
    public ArrayList<ClientHandler> getClients() {
        return clients;
    }


    /**
     * Move the client to a room selected, if not exists, create one.
     * @param roomname
     * @param client
     */
    public void joinRoom(String roomname, ClientHandler client) {
        removeClient(client);

        boolean found= false;
        //Check if exists a chatroom with the name writed.
        for (Map.Entry<String, ArrayList<ClientHandler>> chatroom : chatRooms.entrySet()) {
            String key = chatroom.getKey();
            //If exists, add the client to the array
            if (key.equals(roomname.toLowerCase())) {

                chatroom.getValue().add(client);
                found = true;
                LOGGER.info(client.getName()+" has joined to "+ roomname);
            }
        }
        //If the chatroom doesn't exists, create a new one and add the client.
        if (found==false){
           ArrayList<ClientHandler> chatRoomClient = new ArrayList<>();
           chatRoomClient.add(client);
            chatRooms.put(roomname.toLowerCase(), chatRoomClient);
            LOGGER.info(roomname+ " has been created");
        }

    }


    /**
     *List all the rooms available to a client
     * @param client
     */
    public void listRoom(ClientHandler client){
        String chatslist="Rooms avaliable: ";
        for (Map.Entry<String, ArrayList<ClientHandler>> chatroom : chatRooms.entrySet()) {
            String key = chatroom.getKey();
            var value = chatroom.getValue();

         chatslist += " ("+ key +"[" + value.size()+"])  ";
        }
        client.sendMessage(chatslist);
        LOGGER.info(chatslist);
    }


    /**
     *Move the client from the current room to "general"
     * @param client
     */
    public void leaveRoom(ClientHandler client){
        removeClient(client);
        chatRooms.get("general").add(client);
        LOGGER.info(client.getName()+" left from the current room");

    }


    /**
     *Find the client in all the rooms, remove it from
     * the current one, delete it if it's empty.
     * @param client
     */
    public void removeClient(ClientHandler client){
        //Check all the rooms
        for (Map.Entry<String, ArrayList<ClientHandler>> chatroom : chatRooms.entrySet()) {
            String key = chatroom.getKey();
            var value = chatroom.getValue();
            //Removes the client from the actual one.
            if (value.contains(client)){
                value.remove(client);
                LOGGER.info(client.getName()+" removed from "+ key);
                if (value.isEmpty() && (!chatroom.equals(chatRooms.get("general"))))
                    chatRooms.remove(key);
                LOGGER.info(key+" room deleted");
            }
        }
    }

    public void disconnectOfflineClients() {
        var disconnected = clients.stream().filter(c -> !c.isConnected()).toList();

        if (disconnected.size() > 0) {
            LOGGER.info("[Server]\tRemoving " + disconnected.size() + " disconnected clients");
            clients.removeAll(disconnected);
        }
    }
}
