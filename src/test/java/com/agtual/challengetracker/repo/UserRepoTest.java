package com.agtual.challengetracker.repo;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.agtual.challengetracker.entity.User;

@DataJpaTest
public class UserRepoTest {

    @Autowired
    UserRepo repo;

    @Test
    void testUniqueConstraints() {
        String authSubject = "auth|1234";
        String email = "bob@gmail.com";
        User user1 = craeteValidUser(authSubject, email);
        User user2 = craeteValidUser(authSubject, email);

        repo.saveAndFlush(user1);
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(user2));
    }

    @Test
    void testUsersCantHaveSameEmail() {
        String email = "bob@gmail.com";
        User user1 = craeteValidUser("auth|1234", email);
        User user2 = craeteValidUser("auth|9876", email);

        repo.saveAndFlush(user1);
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(user2));
    }

    @Test
    void testusersCantHaveSameAuthSubject() {
        String authSubject = "auth|1234";
        User user1 = craeteValidUser(authSubject, "bob@gmail.com");
        User user2 = craeteValidUser(authSubject, "steve@gmail.com");

        repo.saveAndFlush(user1);
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(user2));
    }

    @Test
    void testCantSaveNullAuthSubject() {
        User user = new User();
        user.setEmail("x@gmail.com");
        user.setFirstName("Jack");
        user.setLastName("Smith");

        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(user));
    }

    @Test
    void testCantSaveNullEmail() {
        User user = new User();
        user.setAuthSubject("auth|1234");
        user.setFirstName("Jack");
        user.setLastName("Smith");

        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(user));
    }

    @Test
    void testCantSaveNullFirstName() {
        User user = new User();
        user.setAuthSubject("auth|1234");
        user.setEmail("x@gmail.com");
        user.setLastName("Smith");

        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(user));
    }

    @Test
    void testCantSaveNullLastName() {
        User user = new User();
        user.setAuthSubject("auth|1234");
        user.setEmail("x@gmail.com");
        user.setFirstName("Jack");

        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(user));
    }

    private User craeteValidUser(String authSubject, String email) {
        User user = new User();
        user.setAuthSubject(authSubject);
        user.setEmail(email);
        user.setFirstName("John");
        user.setLastName("Doe");
        return user;
    }
}
