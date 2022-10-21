package com.ateam;

/**
 * A simple class representing a user with a username and a hashed password.
 *
 * @author tsasmilex
 * @version 1.0
 */
public class User {
    private String username;
    private String hashedPassword;
    private int lives = 3;
    private boolean banned = false;

    public User (String username, String hashedPassword) {
        this.username       = username;
        this.hashedPassword = hashedPassword;
        
    }
    
        public User (String username, String hashedPassword, int lives, boolean banned) {
        this.username       = username;
        this.hashedPassword = hashedPassword;
        this.banned         = banned;
        this.lives          = lives;
        
    }


    public String getUsername() {
        return username;
    }


    public String getHashedPassword() {
        return hashedPassword;
    }

    public int getLives() {
        return lives;
    }

    public boolean isBanned() {
        return banned;
    }

    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        User o = (User) obj;
        return o.getUsername() == this.getUsername() && o.getHashedPassword() == this.getHashedPassword();
    }


    @Override
    public int hashCode() {
        return this.username.hashCode() + this.hashedPassword.hashCode();
    }
    
    public void substractLifes(){
    
        if (lives > 0) {            
            lives--;
        }  else {
            banned = true;
        }
    }
   
}
    
    
    
    
    

