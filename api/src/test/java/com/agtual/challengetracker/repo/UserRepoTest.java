package com.agtual.challengetracker.repo;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.testutil.PostgresJpaBaseTest;

@Testcontainers
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class UserRepoTest extends PostgresJpaBaseTest {

    @Autowired
    UserRepo repo;

    @Test
    void testUsersCantHaveSameEmail() {
        String email = "bob@gmail.com";
        User user1 = createValidUser("auth|1234", email, "user1");
        User user2 = createValidUser("auth|9876", email, "user2");

        repo.saveAndFlush(user1);
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(user2));
    }

    @Test
    void testCantSaveNullEmail() {
        User user = new User();
        user.setAuthSubject("auth|1234");
        user.setFirstName("Jack");
        user.setLastName("Smith");

        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(user));
    }

    private User createValidUser(String authSubject, String email, String username) {
        User user = new User();
        user.setAuthSubject(authSubject);
        user.setEmail(email);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername(username);
        return user;
    }
}
