package com.ateam;

import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App
{
    private static Logger LOGGER = Logger.getLogger("App");
    public static void main( String[] args )
    {
        
        LOGGER.info("Hola mundo");
        try {
            Server.run(args);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println( "Hello World!" );
    }
}
