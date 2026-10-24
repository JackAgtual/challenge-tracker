package com.agtual.challengetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.agtual.challengetracker.dto.request.CreateUserRequest;
import com.agtual.challengetracker.dto.request.UserAccountSetupRequest;
import com.agtual.challengetracker.entity.Avatar;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.exception.AlreadyExistsException;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.AvatarRepo;
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
    static String validAvatarSrc = "/avatars/pic.png";

    @MockitoSpyBean
    UserRepo userRepo;

    @Autowired
    UserService userService;

    @Autowired
    AvatarRepo avatarRepo;

    User existingUser;
    User userToCreate;

    @BeforeEach
    void beforeEach() {
        User existingUserNotSaved = new User(EXISTING_SUBJECT, existingUserRequest);
        userToCreate = new User(NEW_SUBJECT, newUserRequest);
        existingUser = userRepo.save(existingUserNotSaved);

        Avatar avatar = new Avatar();
        avatar.setSrc(validAvatarSrc);
        avatarRepo.save(avatar);
    }

    @Test
    void testCreateUser() {
        User created = userService.createUser(newJwt, newUserRequest);
        User userFromRepo = userRepo.findByAuthSubject(created.getAuthSubject()).get();
        assertUserEquality(userToCreate, userFromRepo);
    }

    @Test
    void testCreateUserReturnsExistingUserIfUserAlreadyExists() {
        User existingUser = userService.createUser(existingJwt, existingUserRequest);
        assertEquals(userRepo.findByAuthSubject(existingJwt.getSubject()).get(), existingUser);
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
        UserAccountSetupRequest accountSetup = new UserAccountSetupRequest(firstName, lastName, username,
                validAvatarSrc);
        userService.finishAccountSetup(existingUser, accountSetup);

        User userFromRepo = userRepo.findByAuthSubject(existingUser.getAuthSubject()).get();

        // check account setup fields
        assertEquals(firstName, userFromRepo.getFirstName());
        assertEquals(lastName, userFromRepo.getLastName());
        assertEquals(username, userFromRepo.getUsername());
        assertEquals(validAvatarSrc, userFromRepo.getAvatarSrc());

        // make sure existing fields weren't modified
        assertEquals(existingUser.getAuthSubject(), userFromRepo.getAuthSubject());
        assertEquals(existingUser.getEmail(), userFromRepo.getEmail());
    }

    @Test
    void testFinishAccountSetupWorksWithNullAvatarSrc() {
        String firstName = "Bob";
        String lastName = "Smith";
        String username = "bsmith12";
        UserAccountSetupRequest accountSetup = new UserAccountSetupRequest(firstName, lastName, username, null);
        userService.finishAccountSetup(existingUser, accountSetup);

        User userFromRepo = userRepo.findByAuthSubject(existingUser.getAuthSubject()).get();

        // check account setup fields
        assertEquals(firstName, userFromRepo.getFirstName());
        assertEquals(lastName, userFromRepo.getLastName());
        assertEquals(username, userFromRepo.getUsername());
        assertNull(userFromRepo.getAvatarSrc());

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
        UserAccountSetupRequest setup = new UserAccountSetupRequest("jack", "a", username, validAvatarSrc);
        assertThrows(AlreadyExistsException.class, () -> userService.finishAccountSetup(savedNewUser, setup));
    }

    @Test
    void testFinishAccountSetupUserCanReuseTheirOwnUsername() {
        String firstName = "jack";
        String lastName = "a";
        String username = "user1";

        existingUser.setUsername(username);
        userRepo.save(existingUser);

        UserAccountSetupRequest setup = new UserAccountSetupRequest(firstName, lastName, username, validAvatarSrc);
        userService.finishAccountSetup(existingUser, setup);

        User updatedUser = userRepo.findByAuthSubject(existingUser.getAuthSubject()).get();
        assertEquals(firstName, updatedUser.getFirstName());
        assertEquals(lastName, updatedUser.getLastName());
        assertEquals(username, updatedUser.getUsername());
    }

    @Test
    void testFinishAccountSetupRequiresValidAvatar() {
        UserAccountSetupRequest setup = new UserAccountSetupRequest("bob", "stone", "bob_stone_123",
                "invalid avatar src");
        assertThrows(NotFoundException.class, () -> userService.finishAccountSetup(existingUser, setup));
    }

    @Test
    void testSearchUserOnlyReturnsSetupUsers() {
        User setupUser1 = mock(User.class);
        User setupUser2 = mock(User.class);
        User notSetupUser = mock(User.class);

        when(setupUser1.isAccountSetup()).thenReturn(true);
        when(setupUser2.isAccountSetup()).thenReturn(true);
        when(notSetupUser.isAccountSetup()).thenReturn(false);

        doReturn(List.of(setupUser1, setupUser2, notSetupUser)).when(userRepo).searchUser(anyString());

        List<User> users = userService.searchUser("john doe");
        assertEquals(2, users.size());
        assertTrue(users.containsAll(List.of(setupUser1, setupUser2)));
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
