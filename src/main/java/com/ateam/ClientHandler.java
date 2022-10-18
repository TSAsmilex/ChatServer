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
import java.net.SocketException;
import java.util.ArrayDeque;

/**
 *
 * @author pferna12
 */
public class ClientHandler extends Thread {
    ArrayDeque<String> messages = new ArrayDeque<>();
    String lastMessage = new String();

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
     * @throws java.io.IOException
     */
    public ClientHandler(Socket socket) throws IOException {
        super();

        System.out.println("[ClientHandler] New socket detected with IP " + socket.getInetAddress() + ". Creating ClientHandler");

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
        while (true) {
            // If there are no messages pending => wake up a thread to await for a new one
            if (lastMessage.isEmpty()) {
                if (!awaitMessageThread.isAlive()) {
                    awaitMessageThread.start();
                }
            }
            // New message received. Save it to the queue and clean it.
            else {
                awaitMessageThread = new Thread(awaitMessage, "Await message");
                messages.add(new String(lastMessage));
                lastMessage = new String();
            }


            try {
                Thread.sleep(200);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public boolean isConnected() {
        return socket.isConnected();
    }


    public void close() {
        try {
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
