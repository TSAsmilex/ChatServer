package com.ateam;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ClientHandlerAwaitMessage extends Thread {
    BufferedReader reader;
    Socket socket;
    String message = new String();

    ClientHandlerAwaitMessage(Socket socket, BufferedReader reader) {
        this.reader = reader;
        this.socket = socket;
    }

    /**
     *
     * @throws IOException
     */
    public void awaitMessage() throws IOException {
        // Output stream to the client
        // Local reader from the client
        String clientRequest;

        // Get request from client
        System.out.println("[ClientHandlerAwaitMessage]\t Awaiting message");
        clientRequest = reader.readLine();
        message = clientRequest;
        System.out.println("[ClientHandlerAwaitMessage]\t Message received!" + message.length());
    }

    @Override
    public void run () {
        try {
            awaitMessage();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty() {
        return message.isEmpty();
    }

    public String getMessage() {
        return this.message;
    }
}
