/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ateam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class ClientHandler extends Thread {

    ArrayDeque<String> messages = new ArrayDeque<>();
    String lastMessage = new String();
    private static final Logger LOGGER = Logger.getLogger("ClientHandler");

    Socket socket;
    BufferedReader reader;
    PrintStream writer;

    Runnable awaitMessage = () -> {
        System.out.println("[ClientHandler]\t Awaiting message");

        try {
            lastMessage = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ClientHandlerAwaitMessage]\t Message received, with length " + lastMessage.length());
    };

    Thread awaitMessageThread = new Thread(awaitMessage, "Await message");

    /**
     *
     * @param socket
     * @throws com.ateam.ClientHandlerException
     */
    public ClientHandler(Socket socket) throws ClientHandlerException {
        super();
        System.out.println("[ClientHandler] New socket detected with IP " + socket.getInetAddress() + ". Creating ClientHandler");
        this.socket = socket;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "BufferedReader failed", e);
            throw new ClientHandlerException("Failure in ClientHandler. Cannot build object.");
        }

        try {
            writer = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "PrintStream failed", e);
            throw new ClientHandlerException("Cannot build object. Failure in ClientHandler.");
        }

    }

    /**
     * Get messages in newQueue
     * @return a new cleaned ArrayDeque
     */
    public ArrayDeque<String> getMessages() {
        ArrayDeque<String> newQueue = new ArrayDeque<>();
        for (String msg : messages) {
            newQueue.add(msg);
        }
        messages.clear();
        return newQueue;
    }

    /**
     *
     * @param message
     * @throws com.ateam.ClientHandlerException
     *
     */
    public void sendMessage(String message) throws ClientHandlerException {
        try {
            // Send response to client
            writer.println(message);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "PrintStream failed", e);
            throw new ClientHandlerException("Failure in ClientHandler. Check PrintStream.");
        }

    }

    /**
     *
     * @return
     */
    public boolean checkPendingMessages() {
        return !this.messages.isEmpty();
    }

    /**
     *
     */
    @Override
    public void run() {
        while (true) {
            // If there are no messages pending => wake up a thread to await for a new one
            if (lastMessage.isEmpty()) {
                if (!awaitMessageThread.isAlive()) {
                    awaitMessageThread.start();
                }
            } // New message received. Save it to the queue and clean it.
            else {
                awaitMessageThread = new Thread(awaitMessage, "Await message");
                messages.add(new String(lastMessage));
                lastMessage = new String();
            }

            try {
                Thread.sleep(200);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Run failed", e);
            }
        }
    }

    /**
     *
     * @return
     */
    public boolean isConnected() {
        return socket.isConnected();
    }

    /**
     *
     * @throws ClientHandlerException
     */
    public void close() throws ClientHandlerException {
        try {
            socket.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error closing thread.", e);
        }
    }
}
