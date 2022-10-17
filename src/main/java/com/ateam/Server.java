package com.ateam;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
    final int PORT = 49080;

    private ArrayList<Socket> clients = new ArrayList<Socket>();

    ServerSocket ss ;


    public void run(String[] args) throws Exception {
            Socket         socket       = null;

            System.out.println("TCP Server is starting up, listening at port " + PORT + ".");

            while (true) {
                // Get request from client
                socket        = ss.accept();


            }
    }
}