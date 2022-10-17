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

/**
 *
 * @author pferna12
 */
public class ClientHandler extends Thread {

    ArrayDeque<String> messages = new ArrayDeque<>();
    Socket socket;
    BufferedReader reader;
    PrintStream writer;

    /**
     *
     * @param socket
     */
    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintStream(socket.getOutputStream());
    }

    /**
     *
     * @return
     */
    public ArrayDeque<String> getMessages() {
        ArrayDeque<String> newQueue = new ArrayDeque<>();
        for(String msg : messages){
            newQueue.add(msg);
        }
        messages.clear();
        return newQueue;
    }

    /**
     *
     * @throws IOException
     */
    public void awaitMessage() throws IOException {
        // Output stream to the client
        // Local reader from the client

        String clientRequest;

        while (true) {
            // Get request from client

            clientRequest = reader.readLine();
            messages.add(clientRequest);
            System.out.println("[TCPServer] Get request [" + clientRequest + "] from Client.");
        }
    }

    /**
     *
     * @param message
     * @throws IOException
     *
     */
    public void sendMessage(String message) throws IOException {

        // Send response to client
        writer.println(message);
    }

    public boolean checkPendingMessages() {
        return !messages.isEmpty();
    }
}
