package com.ateam;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class UserDBTest {
    @Test
    public void testAddUserIfDBIsEmpty() {
        UserDB db = new UserDB();
        User user = new User("name1", "password1");

        db.addUser(user);

        assert(db.getUsers().contains(user));
    }

    @Test
    public void testAddUserWhichAlreadyExists() {
        UserDB db = new UserDB();
        User user = new User("name1", "password1");

        db.addUser(user);

        assert(db.addUser(user) == false);
    }

    @Test
    public void testExists() {
        UserDB db = new UserDB();
        User user = new User("name1", "password1");

        db.addUser(user);

        assert(db.exists(user.getUsername()) == true);
    }

    @Test
    public void testLoadDB() {
        UserDB db = new UserDB();
        User user = new User("name1", "password1");

        db.addUser(user);

        try {
            db.loadDB();
        }
        catch (FileNotFoundException e) {
            fail("The database " + UserDB.DB_FILEPATH + " does not exist.");
        }
        catch (IOException e) {
            fail("The database " + UserDB.DB_FILEPATH + " does not exist.");
        }

        assert(db.getUsers().contains(user));
    }

    @Test
    public void testLogin() {
        UserDB db = new UserDB();
        User user = new User("name1", "password1");

        db.addUser(user);

        try {
            assert((db.login(user.getUsername(), user.getHashedPassword()).equals(user)));
        }
        catch (Exception e) {
            fail("The user " + user.getUsername() + " does not exist.");
        }
    }

    @Test
    public void testWriteDB() throws FileNotFoundException, IOException {
        UserDB db   = new UserDB();
        User   user = new User("name", "password");
        String path = new String("./db/userTest.csv");

        db.addUser(user);

        try {
            db.writeDB(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File csvFile = new File(path);

        assert(csvFile.exists());

        String username = new String(), password = new String();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] userString = line.split(";");

                username       = userString[0];
                password = userString[1];
            }
        }

        assert(username.equals("name"));
        assert(password.equals("password"));

        csvFile.delete();
    }
}
