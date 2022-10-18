package com.ateam;

import java.util.ArrayList;

/**
 * This class contains the attributes and methods of an User.
 *
 * @author
 * @version 1.0
 */
public class User {
    private String username;
    private String hashedPassword;

    public User (String username, String hashedPassword) {
        this.username       = username;
        this.hashedPassword = hashedPassword;
    }


    public String getUsername() {
        return username;
    }

    /**
     * getHashedPassword
     *
     * @return hashedPassword
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        User o = (User) obj;
        return o.getUsername() == this.username && o.getHashedPassword() == this.getHashedPassword();
    }
}
