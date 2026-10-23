package com.agtual.challengetracker.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.agtual.challengetracker.dto.request.UserAccountSetupRequest;

public class UserTest {
    @Test
    void testIsAccountSetup() {
        User user = new User();
        assertFalse(user.isAccountSetup());

        user.setUsername("");
        user.setFirstName("");
        user.setLastName("");
        assertFalse(user.isAccountSetup());

        user.setUsername("user1");
        assertFalse(user.isAccountSetup());

        user.setUsername("");
        user.setFirstName("Bob");
        assertFalse(user.isAccountSetup());

        user.setFirstName("");
        user.setLastName("Smith");
        assertFalse(user.isAccountSetup());

        user.setUsername("bsmith1");
        user.setFirstName("Bob");
        user.setLastName("Smith");
        assertTrue(user.isAccountSetup());
    }

    @Test
    void testSetupAccount() {
        String firstName = "Bob";
        String lastName = "Smith";
        String usernamne = "bsmith2";

        User user = new User();
        UserAccountSetupRequest setup = new UserAccountSetupRequest(firstName, lastName, usernamne);
        user.setupAccount(setup);

        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(usernamne, user.getUsername());

    }

    @Test
    void testGetAvatarSrcNull() {
        User user = new User();
        assertNull(user.getAvatarSrc());
    }

    @Test
    void testGetAvatarSrcValid() {
        String src = "/avatars/pic.png";

        Avatar avatar = new Avatar();
        avatar.setSrc(src);
        User user = new User();
        user.setAvatar(avatar);

        assertEquals(src, user.getAvatarSrc());
    }
}
