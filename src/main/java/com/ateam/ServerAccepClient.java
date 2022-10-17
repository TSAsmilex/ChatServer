package com.ateam;

import java.io.*;
import java.net.*;

/**
 * Manages the connection to new clients.
 */

public class ServerAccepClient extends Thread {
    private Socket currentSocket = null;
    private ServerSocket ss = null;

    /**
     * Constructor for the class.
     * @param ss the server socket of the server.
     */
    ServerAccepClient (ServerSocket ss) {
        super();
        this.ss = ss;
    }

    /**
     * Wait for a new connection. The attribute {@code currentSocket} will be overriden once
     * a new connection is accepted.
     */
    @Override
    public void run() {
        while (true) {
            if (currentSocket == null) {
                try {
                    currentSocket = ss.accept();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * Get the current socket.
     * @return the current socket.
     */
    public Socket getSocket() {
        Socket socket = currentSocket;
        currentSocket = null;
        return socket;
    }

    /**
     * Check if there is a new socket available.
     * @return true if a new connection has been accepted.
     */
    public boolean hasSocket() {
        return currentSocket != null;
    }
}
