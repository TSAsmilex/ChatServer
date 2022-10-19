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

    public String getLastMessage(){
        return lastMessage;
    }

    Thread awaitMessageThread = new Thread(awaitMessage, "Await message");

    /**
     * ClientHandler constructor
     *
     * @param socket
     * @throws com.ateam.ClientHandlerException
     */
    public ClientHandler(Socket socket) throws ClientHandlerException {
        super();
        LOGGER.log(Level.INFO, "[ClientHandler] New socket detected with IP {0}. Creating ClientHandler",
                socket.getInetAddress());
        this.socket = socket;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "[ClientHandler]\t BufferedReader failed", e);
            throw new ClientHandlerException("Failure in ClientHandler. Cannot build object.");
        }

        try {
            writer = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "[ClientHandler]\t PrintStream failed", e);
            throw new ClientHandlerException("Failure in ClientHandler. Cannot build object.");
        }
    }

    public ClientHandler(Socket socket, BufferedReader reader, PrintStream writer) {
        super();
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    /**
     * Get messages in newQueue
     *
     * @return a new cleaned ArrayDeque
     */
    public ArrayDeque<String> getMessages() {
        if (!lastMessage.isEmpty()) {
            messages.add(lastMessage);
            lastMessage = new String();
        }

        ArrayDeque<String> newQueue = new ArrayDeque<>();
        for (String msg : messages) {
            newQueue.add(msg);
        }
        messages.clear();
        return newQueue;
    }

    /**
     * Send a message to the client
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
            LOGGER.log(Level.SEVERE, "[ClientHandler]\t PrintStream failed", e);
            throw new ClientHandlerException("Failure in ClientHandler. Check PrintStream.");
        }

    }

    /**
     * Check if there are pending messages
     *
     * @return false if there aren't messages
     */
    public boolean checkPendingMessages() {
        return !this.messages.isEmpty();
    }

    /**
     * main method
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
                LOGGER.log(Level.SEVERE, "[ClientHandler]\t Run failed", e);
            }
        }
    }

    /**
     * check connection
     *
     * @return
     */
    public boolean isConnected() {
        return socket.isConnected();
    }

    /**
     * close existing connection
     *
     * @throws ClientHandlerException
     */
    public void close() throws ClientHandlerException {
        try {
            socket.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "[ClientHandler]\t Error closing thread.", e);
        }
    }
}
