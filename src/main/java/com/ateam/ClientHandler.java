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
    ClientHandlerAwaitMessage awaiter;

    Socket socket;
    BufferedReader reader;
    PrintStream writer;

    /**
     *
     * @param socket
     * @throws java.io.IOException
     */
    public ClientHandler(Socket socket) throws IOException {
        super();
        this.socket = socket;
        System.out.println("[ClientHandler] New socket detected " + socket.getInetAddress());
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintStream(socket.getOutputStream());

        awaiter = new ClientHandlerAwaitMessage(socket, reader);
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
     * @param message
     * @throws IOException
     *
     */
    public void sendMessage(String message) throws IOException {
        // Send response to client
        writer.println(message);
    }


    public boolean checkPendingMessages() {
        return this.messages.size() > 0;
    }

    @Override
    public void run() {
        // FIXME esto peta.
        while (true) {
            if (awaiter.isEmpty()) {
                awaiter.start();
            }
            else {
                System.out.println("[ClientHandler] Mensaje recibido");
                messages.add(awaiter.getMessage());
            }
        }
    }
}
