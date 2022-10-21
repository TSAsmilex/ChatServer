package com.ateam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

public class UserDB {
    static final String DB_FILEPATH = "./db/user.csv";
    private static final Logger LOGGER = Logger.getLogger("UserDB");

    private HashSet<User> users = new HashSet<>();

    /**
     *
     *
     * Constructor
     */
    public UserDB() {
        try {
            loadDB();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.INFO, "The database " + DB_FILEPATH + " does not exist.");
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "The database " + DB_FILEPATH + " does not exist.");
        }
    }

    /**
     *
     * get the user from the database
     */
    public HashSet<User> getUsers() {
        return users;
    }

    /**
     *
     * Load the database from the file
     *
     * @return true if the file exists. Otherwise false
     * @throws IOException
     * @throws FileNotFoundException
     */

     public String getPassword(String username) throws LoginException {
        Optional<User> user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
        if (user.isPresent()) {
            return user.get().getHashedPassword();
        } else {
            throw new LoginException("User does not exist");
        }
    }


    public boolean loadDB() throws FileNotFoundException, IOException {
        File csvFile = new File(UserDB.DB_FILEPATH);

        if (!csvFile.exists()) {
            LOGGER.warning("[UserDB]\tThe database file could not be found.");
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] userString = line.split(";");

                String username = userString[0];
                String hashedPassword = userString[1];
                String lives= userString[2];                
                
                User user = new User(username, hashedPassword, Integer.parseInt(lives));

                this.users.add(user);
            }
        }

        return true;
    }

    /**
     * Tries to save the database to a file.
     *
     * @return true if the file was saved successfully. Otherwise false
     * @throws IOException
     */
    public boolean writeDB(String path) throws IOException {
        String output = this.users.stream()
                .map(user -> new String(
                        user.getUsername() + ";"
                    +   user.getHashedPassword() + ";"
                    +   user.getLives()))
                .collect(Collectors.joining("\n"));

        File csvFile = new File(path);

        if (!csvFile.exists()) {
            csvFile.getParentFile().mkdirs();
            csvFile.createNewFile();
        }

        try (PrintWriter pw = new PrintWriter(csvFile)) {
            pw.println(output);
        }

        return csvFile.exists();
    }

    /**
     * Tries to login the user
     *
     * @param user the user to be added
     * @return false if the user already exists in the database.
     */
    public boolean addUser(User user) {
        if (exists(user.getUsername())) {
            return false;
        }

        return this.users.add(user);
    }

    /**
     * Checks if a certain username exists in the database.
     *
     * @param username the username to be checked
     * @return true if the username exists in the database. Otherwise false
     */
    public boolean exists(String username) {
        return this.users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .isPresent();
    }

    /**
     * Tries to login the user
     *
     * @param username
     * @param password
     * @return false if the credentials were wrong.
     * @throws LoginException
     */
    public User login(String username, String password) throws LoginException {
        Optional<User> user = this.users.stream()
                .filter(u -> u.getUsername().equals(username)
                        && u.getHashedPassword().equals(password))
                .findFirst();

        if (!user.isPresent()) {
            throw new LoginException("Invalid credentials");
        } 

        return user.get();
    }
}
