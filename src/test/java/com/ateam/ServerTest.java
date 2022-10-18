/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.ateam;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;


/**
 *
 * @author pferna12
 */
public class ServerTest {
    public ServerTest() {
    }

    @Test
    public void testBroadcastDoesNotSendToSelf() throws Exception {
        Server server = new Server();
        server.run();

        ClientHandler client1 = new ClientHandler(null);
        Mockito.mock(null);
        // client1 mete un mensaje.
        ClientHandler client2 = new ClientHandler(null);

        server.broadcast(client1);

        assertTrue(!client1.checkPendingMessages());
        assertTrue(client2.checkPendingMessages());
    }
}
