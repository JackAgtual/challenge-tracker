package com.agtual.challengetracker.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.agtual.challengetracker.entity.Avatar;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.testutil.PostgresJpaBaseTest;

public class UserRepoIT extends PostgresJpaBaseTest {

    @Autowired
    UserRepo repo;

    @Autowired
    AvatarRepo avatarRepo;

    @Test
    void testUsersCantHaveSameEmail() {
        String email = "bob@gmail.com";
        User user1 = createValidUser("auth|1234", email, "user1");
        User user2 = createValidUser("auth|9876", email, "user2");

        repo.saveAndFlush(user1);
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(user2));
    }

    @Test
    void testCantHaveSameAuthSubject() {
        String authSubject = "auth|1234";
        User user1 = createValidUser(authSubject, "jack@gmail.com", "jj12");
        User user2 = createValidUser(authSubject, "bob@gmail.com", "bob_123");
        repo.save(user1);
        assertThrows(DataIntegrityViolationException.class, () -> repo.save(user2));
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
    void testAvatarSrcMustBeValidIdInAvatarsTable() {
        Avatar avatar = new Avatar();
        avatar.setSrc("/avatars/image.png");
        avatarRepo.save(avatar);

        User userBadSrc = new User();
        userBadSrc.setAuthSubject("auth|1234");
        userBadSrc.setEmail("bob@gmail.com");
        userBadSrc.setAvatarSrc("an invalid avatar src");

        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(userBadSrc));
    }

    @Test
    void testCanSaveValidAvatarSrc() {
        String src = "/avatars/image.png";

        Avatar avatar = new Avatar();
        avatar.setSrc(src);
        avatarRepo.save(avatar);

        User user = new User();
        user.setAuthSubject("auth|1234");
        user.setEmail("bob@gmail.com");
        user.setAvatarSrc(src);

        repo.save(user);

        // Will have 2 entities because base class saves a user
        assertEquals(2, repo.count());
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
