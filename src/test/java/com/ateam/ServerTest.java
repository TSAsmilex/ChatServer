/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.ateam;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pferna12
 */
public class ServerTest {
    
    public ServerTest() {
    }

    /**
     * Test of run method, of class Server.
     */
    @Test
    public void testRun() throws Exception {
        // GIVEN 
        System.out.println("run");
        String[] args = null;
        
        // THEN
        Server.run(args);
        
        // EXPECT
    }
    
}
