package com.agtual.challengetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;

import com.agtual.challengetracker.dto.request.CreateUserRequest;
import com.agtual.challengetracker.dto.request.UserAccountSetupRequest;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.exception.AlreadyExistsException;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.UserRepo;

@DataJpaTest
@Import(UserService.class)
public class UserServiceTest {

    static String EXISTING_SUBJECT = "existingSubject";
    static String NEW_SUBJECT = "newSubject";
    static Jwt existingJwt = buildJwt(EXISTING_SUBJECT);
    static Jwt newJwt = buildJwt(NEW_SUBJECT);
    static CreateUserRequest existingUserRequest = new CreateUserRequest("alicesmith@gmail.com");
    static CreateUserRequest newUserRequest = new CreateUserRequest("johndoe@gmail.com");

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService userService;

    User existingUser;
    User userToCreate;

    @BeforeEach
    void beforeEach() {
        User existingUserNotSaved = new User(EXISTING_SUBJECT, existingUserRequest);
        userToCreate = new User(NEW_SUBJECT, newUserRequest);
        existingUser = userRepo.save(existingUserNotSaved);
    }

    @Test
    void testCreateUser() {
        User created = userService.createUser(newJwt, newUserRequest);
        User userFromRepo = userRepo.findById(created.getAuthSubject()).get();
        assertUserEquality(userToCreate, userFromRepo);
    }

    @Test
    void testCreateUserReturnsExistingUserIfUserAlreadyExists() {
        User existingUser = userService.createUser(existingJwt, existingUserRequest);
        assertEquals(userRepo.findById(existingJwt.getSubject()).get(), existingUser);
    }

    @Test
    void testGetUser() {
        Optional<User> user = userService.getUser(existingJwt);
        assertTrue(user.isPresent());
        assertUserEquality(existingUser, user.get());
    }

    @Test
    void testGetValidUser() {
        User user = userService.getValidUser(existingJwt);
        assertUserEquality(existingUser, user);
    }

    @Test
    void testGetValidUserThrowsExceptionForInvalidUser() {
        // User defined by newJwt does not exist
        assertThrows(NotFoundException.class, () -> userService.getValidUser(newJwt));
    }

    @Test
    void testGetValidUserByUsername() {
        User res = userService.getValidUser(existingUser.getUsername());
        assertEquals(existingUser, res);
    }

    @Test
    void testGetValidUserByUsernameNotFound() {
        assertThrows(NotFoundException.class, () -> userService.getValidUser("usernameDoesntExist"));
    }

    @Test
    void testFinishAccountSetup() {
        String firstName = "Bob";
        String lastName = "Smith";
        String username = "bsmith12";
        UserAccountSetupRequest accountSetup = new UserAccountSetupRequest(firstName, lastName, username);
        userService.finishAccountSetup(existingUser, accountSetup);

        User userFromRepo = userRepo.findById(existingUser.getAuthSubject()).get();

        // check account setup fields
        assertEquals(firstName, userFromRepo.getFirstName());
        assertEquals(lastName, userFromRepo.getLastName());
        assertEquals(username, userFromRepo.getUsername());

        // make sure existing fields weren't modified
        assertEquals(existingUser.getAuthSubject(), userFromRepo.getAuthSubject());
        assertEquals(existingUser.getEmail(), userFromRepo.getEmail());
    }

    @Test
    void testFinishAccountSetupRepeatUsername() {
        String username = "user1";
        existingUser.setUsername(username);
        userRepo.save(existingUser);

        User savedNewUser = userRepo.save(userToCreate);
        UserAccountSetupRequest setup = new UserAccountSetupRequest("jack", "a", username);
        assertThrows(AlreadyExistsException.class, () -> userService.finishAccountSetup(savedNewUser, setup));
    }

    private static void assertUserEquality(User expected, User actual) {
        assertEquals(expected.getAuthSubject(), actual.getAuthSubject());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
    }

    private static Jwt buildJwt(String subject) {
        return Jwt.withTokenValue("fake-token").subject(subject).header("Bearer token", "1234").build();
    }
}
