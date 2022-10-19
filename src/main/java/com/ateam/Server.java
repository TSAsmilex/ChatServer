package com.ateam;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
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
    private static final Logger LOGGER = Logger.getLogger("Waiting for new connections");
    final int PORT = 49080;
    private UserAuth userAuth;
    private UserDB db = new UserDB();

    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    Socket socket = null;
    ServerSocket ss;

    Runnable awaitNewConnections = () -> {
        try {
            LOGGER.info("[Server]\tWaiting for new connections");
            socket = ss.accept();
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "[Server] Error waiting for connections", e);
        }
    };
    Thread awaitNewConnectionsThread = new Thread(awaitNewConnections, "Accept socket");


    public Server() {
        try {
            ss = new ServerSocket(PORT);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "[Server] Error creating server socket", e);
        }
    }

    /**
     * Entrypoint of the server.
     * @throws Exception
     */
    public void run() throws Exception {
        LOGGER.info("[Server] TCP Server is starting up, listening at port " + PORT + ".");

        while (true) {
            // Setup new connections
            if (socket == null) {
                if (!awaitNewConnectionsThread.isAlive()) {
                    awaitNewConnectionsThread.start();
                }
            }
            else {
                addNewClientHandler(socket, userAuth);
                awaitNewConnectionsThread = new Thread(awaitNewConnections, "Accept socket");
            }

            for (var client: clients) {
                if (!client.isConnected()) {
                    LOGGER.info("[Server]\tClient disconnected. Removing from pool");
                    client.close();
                    clients.remove(client);
                }
                // No -> close connection
                if (client.checkPendingMessages()) {
                    LOGGER.info("[Server]\t Pending messages to be sent");
                    broadcast(client);
                }
            }


            Thread.sleep(1000);
        }
    }

    /**
     * Send a message to all clients
     * @param messages queue of messages to be sent
     */
    // Broadcast should use the client instead of the messages
    public void broadcast(ClientHandler client) throws ClientHandlerException {
        LOGGER.info("[Server]\t Broadcasting messages");
        var messages = client.getMessages();

        while (!messages.isEmpty()) {
            var message = messages.pop();

            var otherClients = clients.stream().filter(c -> c != client).toList();

            for (var otherClient: otherClients) {
                LOGGER.info("[Server]\t Sending message \"" + message + "\" to client " + client.socket.getInetAddress());
                otherClient.sendMessage(message);
            }
        }
    }

    public void addNewClientHandler(Socket socket, UserAuth ua) throws ClientHandlerException {
        ClientHandler client = new ClientHandler(socket, ua);
        this.socket = null;
        clients.add(client);
        client.start();
    }

    public ArrayList<ClientHandler> getClients() {
        return clients;
    }
}
