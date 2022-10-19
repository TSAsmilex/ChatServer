/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.ateam;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;


/**
 *
 * @author pferna12
 */
public class ServerTest {
    public ServerTest() {
    }

    @Test
    public void testBroadcastCleansBufferOfClient() throws Exception {
        Server server = new Server();

        var testSocket1 = new Socket();
        var testSocket2 = new Socket();

        BufferedReader testReader1 = Mockito.mock(BufferedReader.class);
        BufferedReader testReader2 = Mockito.mock(BufferedReader.class);

        PrintWriter testWriter1 = Mockito.mock(PrintWriter.class);
        PrintWriter testWriter2 = Mockito.mock(PrintWriter.class);

        ClientHandler testClient1 = new ClientHandler(testSocket1, testReader1, testWriter1);
        ClientHandler testClient2 = new ClientHandler(testSocket2, testReader2, testWriter2);

        var clients = server.getClients();
        clients.add(testClient1);
        clients.add(testClient2);

        testClient1.lastMessage = new String("TEST");

        server.broadcast(testClient1);
        testClient2.awaitMessageThread.start();

        assertTrue(!testClient1.checkPendingMessages());
        verify(testWriter1, Mockito.times(1));
    }
}
