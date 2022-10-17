/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.ateam;

import java.net.Socket;
import java.util.ArrayDeque;
import org.junit.Test;
import static org.junit.Assert.*;

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
        // GIVEN
        System.out.println("awaitMessage");
        Socket socket = null;
        ClientHandler instance = new ClientHandler(socket);

        // THEN
        instance.awaitMessage();

        // EXPECT
        assertEquals(instance, this);
    }

    /**
     * Test of sendMessage method, of class ClientHandler.
     */
    @Test
    public void testSendMessage() throws Exception {
        // GIVEN
        System.out.println("sendMessage");
        Socket socket = null;
        ClientHandler instance = new ClientHandler(socket);

        //THEN
        instance.sendMessage("Hola mundo");
        
        // EXPECT
        assertEquals("Hola mundo", instance.getMessages().getLast());
    }

    /**
     * Test of checkPendingMessages method, of class ClientHandler.
     */
    @Test
    public void testCheckPendingMessages() {
        // GIVEN
        System.out.println("checkPendingMessages");
        ClientHandler instance = null;
        boolean expResult = false;
        
        //  THEN
        boolean result = instance.checkPendingMessages();
        
        // EXPECT
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
