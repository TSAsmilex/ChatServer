/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ateam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;

/**
 *
 */
public class ClientHandler extends Thread {

    ArrayDeque<String> messages = new ArrayDeque<>();
    String lastMessage = new String();
    private static final Logger LOGGER = Logger.getLogger("ClientHandler");

    Socket socket;
    BufferedReader reader;
    PrintWriter writer;

    private boolean logged;

    private UserAuth userauth;
    private User user;

    Runnable awaitMessage = () -> {
        LOGGER.info("[ClientHandler]\tAwaiting message");

        try {
            lastMessage = reader.readLine();
            LOGGER.info("[ClientHandler]\tMessage received with size " + lastMessage.length() + ".");
        } catch (Exception e) {
            // e.printStackTrace();
            LOGGER.info("[ClientHandler]\tClient has likely disconnected.");
        }
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
    public ClientHandler(Socket socket, UserAuth ua) throws ClientHandlerException {
        super();
        this.socket = socket;
        userauth = ua;

        LOGGER.log(Level.INFO, "[ClientHandler]\tNew socket detected with IP {0}. Creating ClientHandler.", socket.getInetAddress());

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "[ClientHandler]\t BufferedReader failed", e);
            throw new ClientHandlerException("Failure in ClientHandler. Cannot build object.");
        }

        try {
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "[ClientHandler]\t PrintStream failed", e);
            throw new ClientHandlerException("Failure in ClientHandler. Cannot build object.");
        }
    }


    public ClientHandler(Socket socket, BufferedReader reader, PrintWriter writer) {
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
    public void sendMessage(String message) {
        try {
            // Send response to client
            LOGGER.info("Sending message + " + message + " to client " + socket.getInetAddress() + ".");
            writer.println(message);
            writer.flush();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "[ClientHandler]\t PrintStream failed", e);
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
        // while the user isn't logged.
        while (true) {
            // The client might disconnect at any point. If that happens,
            // we cut off the execution early.
            if (!isConnected()) {
                break;
            }

            if (!logged) {
                setupAccount();
            } //if the user is logged
            else {
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
                    LOGGER.log(Level.SEVERE, "[ClientHandler]\tRun failed", e);
                }
            }
        }
    }

    /**
     * check connection
     *
     * @return
     */
    public boolean isConnected() {
        try {
            socket.getOutputStream().write(0);
            return true;
        } catch (IOException e) {
            return false;
        }
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


    public String getUsername() {
        return this.user == null? "" : this.user.getUsername();
    }


    public void setupAccount() {
        sendMessage("Welcome. Please login or register in order to continue.");

        String action = "";
        try {
            action = reader.readLine().toLowerCase().trim();
            LOGGER.info("[ClientHandler]\tReceived " + action);
        } catch (IOException ex) {
            LOGGER.info("[ClientHandler]\tError reading from socket. It is possible that the client has disconnected.");
        }

        if (action.equalsIgnoreCase("login") || action.equalsIgnoreCase("register")) {
            //We ask the username and the pass.
            try {
                sendMessage("User: ");
                String username = reader.readLine();
                sendMessage("Password: ");
                String pass = reader.readLine();

                LOGGER.info("[ClientHandler]\tReceived credentials " + username + ", " + pass);

                if (action.equalsIgnoreCase("login")) {
                    //Check if the user exists with the username/pass getted.
                    this.user = userauth.login(username, pass);
                    LOGGER.info("[ClientHandler]\tUser " + username + " logged in correctly.");
                } else {
                    //Check if the user exists with the username/pass getted.
                    this.user = userauth.registerUser(username, pass);
                    LOGGER.info("[ClientHandler]\tUser " + username + " registered correctly.");
                }
                logged = true;

                //If the credentiales don't match with login(), throws an exception and we catch here.
                sendMessage("successful");
            } catch (LoginException e) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, "Login error", e);
                sendMessage("error");
            } catch (IOException ex) {
                LOGGER.severe("[ClientHandler]\tFail to read the line from client");
            }
        } else {
            sendMessage("Please introduce \"login\" or \"register\".");
        }
    }
}
