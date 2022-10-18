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

    }

    @Test
    public void testLogin() {

    }

    @Test
    public void testWriteDB() throws FileNotFoundException, IOException {
        UserDB db = new UserDB();
        User user = new User("name", "password");

        db.addUser(user);

        try {
            db.writeDB();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File csvFile = new File(UserDB.DB_FILEPATH);

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
