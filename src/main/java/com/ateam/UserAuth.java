package com.ateam;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

/**
 * The systems in charge of handling the login and registration of users.
 *
 * @version 1.0
 */
public class UserAuth {
    private UserDB userDB;
    private static final Logger LOGGER = Logger.getLogger("UserAuth");


    public UserAuth(UserDB userDB) {
        this.userDB = userDB;
    }


    public User login (String username, String password) throws LoginException, UserBannedException {
        String hashedPassword = getSHA512(password);

        User user = null;

        try {
            user = userDB.login(username, hashedPassword);

            if (user.isBanned()) {
                throw new UserBannedException("User banned");
            }
        }
        catch (LoginException e) {
            LOGGER.warning("[UserAuth]\tIncorrect user or/and password");
            throw new LoginException("Incorrect user or/and password");
        }

        return user;
    }


    public User registerUser(String username, String password) throws LoginException {
        String hashedPassword = getSHA512(password);

        User user = new User(username, hashedPassword);

        if (!userDB.addUser(user)) {
            throw new LoginException("User already exists in the database");
        }

        try {
            userDB.writeDB(UserDB.DB_FILEPATH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }


    public String getSHA512(String input){
        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(input.getBytes("utf8"));
            toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toReturn;
    }
}
