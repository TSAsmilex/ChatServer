package com.ateam;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App
{
    private static final Logger LOGGER = Logger.getLogger("App");
    public static void main( String[] args )
    {
        
        LOGGER.info("Hola mundo");
        try {
            Server.run(args);

        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        System.out.println( "Hello World!" );
    }
}
