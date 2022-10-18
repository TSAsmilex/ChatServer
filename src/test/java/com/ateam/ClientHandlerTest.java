/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.ateam;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.BufferedReader;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import org.mockito.Mockito;
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
    @Test
    public void testAwaitMessage() throws Exception {
//        // GIVEN
//        System.out.println("awaitMessage");
//        Socket socket = null;
//        //ClientHandler instance = new ClientHandler(socket);
//
//        // THEN
//        //instance.awaitMessage();
//        // EXPECT
//        assertEquals(instance, this);
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

        //THEN
        instance.sendMessage("Hola mundo");

        // EXPECT
        verify(testWriter, times(1)).println("Hola mundo");
        verify(testReader, times(0)).readLine();
    }

    /**
     * Test of checkPendingMessages method, of class ClientHandler.
     */
//    @Test
//    public void testCheckPendingMessages() {
//         GIVEN
//        System.out.println("checkPendingMessages");
//        ClientHandler instance = null;
//        boolean expResult = false;
//
//          THEN
//        boolean result = instance.checkPendingMessages();
//
//         EXPECT
//        assertEquals(expResult, result);
//         TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getMessages method, of class ClientHandler.
//     */
//    @Test
//    public void testGetMessages() throws IOException {
//        
//    }
//
//    /**
//     * Test of run method, of class ClientHandler.
//     */
//    @Test
//    public void testRun() {
//        System.out.println("run");
//        ClientHandler instance = null;
//        instance.run();
//         TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of isConnected method, of class ClientHandler.
//     */
//    @Test
//    public void testIsConnected() {
//        System.out.println("isConnected");
//        ClientHandler instance = null;
//        boolean expResult = false;
//        boolean result = instance.isConnected();
//        assertEquals(expResult, result);
//         TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of close method, of class ClientHandler.
//     */
//    @Test
//    public void testClose() throws Exception {
//        System.out.println("close");
//        ClientHandler instance = null;
//        instance.close();
//         TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
