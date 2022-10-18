package com.ateam;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/*
 * This class is the server side of the application. It manages multiple connections, waiting for messages to arrive.
 * When one client has sent a new message, it broadcast it to all current clients.
 */

/**
 *
 * @author pferna12
 */

public class Server {
    final int PORT = 49080;

    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    Socket socket = null;
    ServerSocket ss;

    Runnable awaitNewConnections = () -> {
        try {
            System.out.println("[Server]\tWaiting for new connections");
            socket = ss.accept();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    };
    Thread awaitNewConnectionsThread = new Thread(awaitNewConnections, "Accept socket");

    /**
     * Entrypoint of the server.
     * @throws Exception
     */
    public void run() throws Exception {
        ss = new ServerSocket(PORT);
        System.out.println("[Server] TCP Server is starting up, listening at port " + PORT + ".");

        while (true) {
            // Setup new connections
            if (socket == null) {
                if (!awaitNewConnectionsThread.isAlive()) {
                    awaitNewConnectionsThread.start();
                }
            }
            else {
                awaitNewConnectionsThread = new Thread(awaitNewConnections, "Accept socket");
                ClientHandler client = new ClientHandler(socket);
                socket = null;
                clients.add(client);
                client.start();
            }


            //System.out.println("[Server]\t Server running");


            for (var client: clients) {
                if (!client.isConnected()) {
                    System.out.println("[Server]\tClient disconnected. Removing from pool");
                    client.close();
                    clients.remove(client);
                }
                // No -> close connection
                if (client.checkPendingMessages()) {
                    System.out.println("[Server]\t Pending messages to be sent");
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
    private void broadcast(ClientHandler client) throws ClientHandlerException {
        System.out.println("[Server]\t Broadcasting messages");
        var messages = client.getMessages();

        while (!messages.isEmpty()) {
            var message = messages.pop();

            for (var otherClient: clients) {
                if (otherClient != client) {
                    System.out.println("[Server]\t Sending message \"" + message + "\" to client " + client.socket.getInetAddress());
                    client.sendMessage(message);
                }
            }

        }
    }
}
