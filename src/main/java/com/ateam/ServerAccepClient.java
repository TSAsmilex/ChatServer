package com.ateam;

import java.io.*;
import java.net.*;

public class ServerAccepClient extends Thread {
    private Socket currentSocket = null;
    private ServerSocket ss = null;

    ServerAccepClient (ServerSocket ss) {
        this.ss = ss;
    }


/**
 * Wait for a new connection and accept it
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
     *
     * @return the current socket and set it to null
     */
    public Socket getSocket() {
        Socket socket = currentSocket;
        currentSocket = null;
        return socket;
    }

    /**
     *
     * @return true if there is a socket to be accepted
     */
    public boolean hasSocket() {
        return currentSocket != null;
    }
}
