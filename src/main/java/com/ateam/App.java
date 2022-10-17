package com.ateam;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        var server = new Server();
        try {
            server.run();
        }
        catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        System.out.println( "Hello World!" );
    }
}
