/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.ateam;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author pferna12
 */
public class ClientHandlerTest {

    public ClientHandlerTest() {
    }

    /**
     * Test of awaitMessage method, of class ClientHandler.
     */
    @Test(timeout = 4000)
    public void testAwaitMessage() throws Exception {
        System.out.println("awaitMessage");
        Socket socket = Mockito.mock(Socket.class);
        BufferedReader reader = Mockito.mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("Hello");
        ClientHandler instance = new ClientHandler(socket, reader, null);

        //System.out.println("Last message: " + instance.getLastMessage());

        instance.awaitMessageThread.start();
        while (instance.getLastMessage().equals("")) {
            Thread.sleep(100);
        }
        assertEquals("Hello", instance.getLastMessage());
    }

    /**
     * Test of sendMessage method, of class ClientHandler.
     */
    @Test
    public void testSendMessage() throws Exception {
        // GIVEN
        System.out.println("sendMessage");
        PrintWriter testWriter = Mockito.mock(PrintWriter.class);
        BufferedReader testReader = Mockito.mock(BufferedReader.class);
        Socket socket = Mockito.mock(Socket.class);
        InetAddress addr = InetAddress.getByName("127.0.0.1");
        when(socket.getInetAddress()).thenReturn(addr);
        ClientHandler instance = new ClientHandler(socket, testReader, testWriter);
        // THEN
        instance.sendMessage("Hola mundo");
        // EXPECT
        verify(testWriter, times(1)).println("Hola mundo");
        verify(testReader, times(0)).readLine();
    }

    /**
     * Test of checkPendingMessages method, of class ClientHandler.
     *
     * @throws ClientHandlerException
     */
    @Test
    public void testCheckPendingMessages() throws ClientHandlerException {
        // GIVEN
        System.out.println("checkPendingMessages");
        PrintWriter testWriter = Mockito.mock(PrintWriter.class);
        BufferedReader testReader = Mockito.mock(BufferedReader.class);
        Socket socket = Mockito.mock(Socket.class);
        ClientHandler instance = new ClientHandler(socket, testReader, testWriter);

        // THEN
        boolean expResult = false;
        boolean result = instance.checkPendingMessages();

        // EXPECT
        assertEquals(expResult, result);
    }

}
