package com.ateam;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

public class UserDB {
    private static final String DB_FILEPATH = "./db/user.csv";
    private static final Logger LOGGER = Logger.getLogger("UserDB");
    private HashSet<User> users = new HashSet<>();


    public UserDB() {
        try {
            loadDB();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public HashSet<User> getUsers() {
        return users;
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

                String username       = userString[0];
                String hashedPassword = userString[1];

                User user = new User(username, hashedPassword);

                this.users.add(user);
            }
        }

        return true;
    }


    public boolean writeDB() throws IOException {
        String output = this.users.stream()
            .map(user -> new String (
                    user.getUsername() + ";"
                +   user.getHashedPassword()
            )).collect(Collectors.joining("\n"));

        File csvFile = new File(UserDB.DB_FILEPATH);

        if (!csvFile.exists()) {
            csvFile.getParentFile().mkdirs();
            csvFile.createNewFile();
        }

        try (PrintWriter pw = new PrintWriter(csvFile)) {
            pw.println(output);
        }

        return csvFile.exists();
    }


    public boolean addUser(User user) {
        if (exists(user.getUsername())) {
            return false;
        }

        return this.users.add(user);
    }


    public boolean exists (String username) {
        return this.users.stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .isPresent();
    }


    public User login (String username, String password) throws LoginException {
        Optional<User> user = this.users.stream()
            .filter(u -> u.getUsername().equals(username) && u.getHashedPassword().equals(password))
            .findFirst();

        if (!user.isPresent()) {
            throw new LoginException("Credenciales inválidos");
        }

        return user.get();
    }
}
