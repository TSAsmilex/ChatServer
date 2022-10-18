package com.ateam;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UserTest {
    @Test
    public void testEqualsOfSameUser() {
        User user1 = new User("user1", "password1");
        User user2 = new User("user1", "password1");

        assertTrue(user1.equals(user2));
    }

    @Test
    public void testEqualsOfDifferentUser() {
        User user1 = new User("user1", "password1");
        User user2 = new User("user2", "password2");

        assertTrue(!user1.equals(user2));
    }


    @Test
    public void testHashCodeOfDifferentUsersIsNotTheSame() {
        User user1 = new User("user1", "password1");
        User user2 = new User("user2", "password2");

        assert(user1.hashCode() != user2.hashCode());
    }

    @Test
    public void testHashCodeOfSameUsersIsTheSame() {
        User user1 = new User("user1", "password1");
        User user2 = new User("user1", "password1");

        assert(user1.hashCode() == user2.hashCode());
    }
}
