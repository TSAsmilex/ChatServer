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
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author pferna12
 */
public class ServerTest {
    public ServerTest() {
    }

    @Test
    public void testBroadcastCleansBufferOfClient() throws Exception {
        // Server server = new Server();

        // var testSocket1 = new Socket();
        // var testSocket2 = new Socket();

        // BufferedReader testReader1 = Mockito.mock(BufferedReader.class);
        // BufferedReader testReader2 = Mockito.mock(BufferedReader.class);

        // PrintWriter testWriter1 = Mockito.mock(PrintWriter.class);
        // PrintWriter testWriter2 = Mockito.mock(PrintWriter.class);

        // ClientHandler testClient1 = new ClientHandler(testSocket1, testReader1, testWriter1);
        // ClientHandler testClient2 = new ClientHandler(testSocket2, testReader2, testWriter2);

        // // var clients = server.getClients();
        // // clients.add(testClient1);
        // // clients.add(testClient2);

        // testClient1.lastMessage = new String("TEST");

        // Thread.sleep(100);

        // server.broadcast(testClient1);
        // testClient2.awaitMessageThread.start();

        // assertTrue(!testClient1.checkPendingMessages());

        // verify(testWriter2, Mockito.times(1))
        //         .println("[" + testClient1.getUsername() + "] " + "TEST");
    }

    /**
     * Test of run method, of class Server.
     */
    @Test
    public void testRun() throws Exception {
        // System.out.println("run");
        // Server instance = new Server();
        // instance.run();
        // // TODO review the generated test code and remove the default call to fail.
        // fail("The test case is a prototype.");
    }

    /**
     * Test of broadcast method, of class Server.
     */
    @Test
    public void testBroadcast() throws Exception {
        // System.out.println("broadcast");
        // ClientHandler client = null;
        // Server instance = new Server();
        // instance.broadcast(client);
        // // TODO review the generated test code and remove the default call to fail.
        // fail("The test case is a prototype.");
    }

    /**
     * Test of addNewClientHandler method, of class Server.
     */
    @Test
    public void testAddNewClientHandler() throws Exception {
        // System.out.println("addNewClientHandler");
        // Socket socket = null;
        // UserAuth ua = null;
        // Server instance = new Server();
        // instance.addNewClientHandler(socket, ua);
        // // TODO review the generated test code and remove the default call to fail.
        // fail("The test case is a prototype.");
    }

    /**
     * Test of getClients method, of class Server.
     */
    @Test
    public void testGetClients() {
        // System.out.println("getClients");
        // Server instance = new Server();
        // ArrayList<ClientHandler> expResult = null;
        // // ArrayList<ClientHandler> result = instance.getClients();
        // // assertEquals(expResult, result);
        // // TODO review the generated test code and remove the default call to fail.
        // fail("The test case is a prototype.");
    }

    /**
     * Test of disconnectOfflineClients method, of class Server.
     */
    @Test
    public void testDisconnectOfflineClients() {
        // System.out.println("disconnectOfflineClients");
        // Server instance = new Server();
        // instance.disconnectOfflineClients();
        // // TODO review the generated test code and remove the default call to fail.
        // fail("The test case is a prototype.");
    }

    /**
     * Test of joinRoom method, of class Server.
     */
    @Test
    public void testJoinRoom() {
        // System.out.println("joinRoom");
        // String roomname = "";
        // ClientHandler client = null;
        // Server instance = new Server();
        // instance.joinRoom(roomname, client);
        // // TODO review the generated test code and remove the default call to fail.
        // fail("The test case is a prototype.");
    }

    /**
     * Test of listRoom method, of class Server.
     */
    @Test
    public void testListRoom() {
        // System.out.println("listRoom");
        // ClientHandler client = null;
        // Server instance = new Server();
        // instance.listRoom(client);
        // // TODO review the generated test code and remove the default call to fail.
        // fail("The test case is a prototype.");
    }

    /**
     * Test of leaveRoom method, of class Server.
     */
    // @Test
    // public void testLeaveRoom() {
    // System.out.println("leaveRoom");
    // // GIVEN
    // PrintWriter testWriter = Mockito.mock(PrintWriter.class);
    // BufferedReader testReader = Mockito.mock(BufferedReader.class);
    // Socket socket = Mockito.mock(Socket.class);
    // ClientHandler client = new ClientHandler(socket, testReader, testWriter);
    // Server instance = new Server();

    // // THEN
    // instance.leaveRoom(client);
    // boolean expResult = true;
    // boolean result = instance.;

    // // EXPECT
    // assertEquals(expResult, result);
    // }

    /**
     * Test of removeClient method, of class Server.
     */
    @Test
    public void testRemoveClient() {
        // System.out.println("removeClient");

        // // GIVEN
        // PrintWriter testWriter = Mockito.mock(PrintWriter.class);
        // BufferedReader testReader = Mockito.mock(BufferedReader.class);
        // Socket socket = Mockito.mock(Socket.class);
        // ClientHandler client = new ClientHandler(socket, testReader, testWriter);
        // Server instance = new Server();

        // THEN
        // instance.removeClient(client);
        boolean expResult = true;
        // boolean result = instance.getClients().isEmpty();

        // EXPECT
        // assertEquals(expResult, result);
    }

    /**
     * Test of filterBadword method, of class Server.
     */
    @Test
    public void testFilterBadword() {
        // GIVEN
        System.out.println("filterBadword");
        String message = "Eres un puto plasta";
        Server instance = new Server();
        Badwords bw = new Badwords();
        bw.loadBw();
        String expResult = "Eres un p♥♥♥ p♥♥♥♥♥";

        // THEN
        //String result = instance.filterBadword(message, );

        // EXPECT
        //assertEquals(expResult, result);
    }
}
