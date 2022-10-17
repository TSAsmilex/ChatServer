package com.ateam;

import java.io.*;
import java.net.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.logging.Handler;


/*
 * This class is the server side of the application. It manages multiple connections, waiting for messages to arrive.
 * When one client has sent a new message, it broadcast it to all current clients.
 */
public class Server {
    final int PORT = 49080;

    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

    ServerAccepClient newConnections;
    ServerSocket ss;


    /**
     * Entrypoint of the server.
     * @throws Exception
     */
    public void run() throws Exception {
        ss = new ServerSocket(PORT);
        System.out.println("TCP Server is starting up, listening at port " + PORT + ".");
        newConnections = new ServerAccepClient(ss);

        Socket socket = null;
        newConnections.run();

        while (true) {
            // Setup new connections
            if (newConnections.hasSocket()) {
                socket = newConnections.getSocket();
                ClientHandler client = new ClientHandler(socket);
                clients.add(client);
                client.start();
            }

            for (var client: clients) {
                if (client.pendingMessage()) {
                    broadcast(client.getMessages());
                }
            }
        }
    }

    /**
     * Send a message to all clients
     * @param messages queue of messages to be sent
     */
    private void broadcast(ArrayDeque<String> messages) {
        while (!messages.isEmpty()) {
            var message = messages.pop();

            for (var client: clients) {
                client.sendMessage(message);
            }

        }
    }
}
