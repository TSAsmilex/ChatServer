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

    /**
     *
     * @param args
     */
    public static void main( String[] args )
    {
        var server = new Server();

        try {
            server.run();
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }
}
