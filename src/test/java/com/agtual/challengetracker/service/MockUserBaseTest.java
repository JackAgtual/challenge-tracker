package com.agtual.challengetracker.service;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.repo.UserRepo;

public abstract class MockUserBaseTest {
    User savedUser;

    @Autowired
    UserRepo userRepo;

    @BeforeEach
    void saveUserToRepo() {
        savedUser = saveRandomUser();
    }

    protected User saveRandomUser() {
        User user = new User();
        user.setEmail("user-" + UUID.randomUUID() + "@gmail.com");
        User userEntity = userRepo.save(user);
        return userEntity;
    }
}
