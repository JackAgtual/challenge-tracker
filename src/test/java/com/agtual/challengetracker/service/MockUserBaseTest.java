package com.agtual.challengetracker.service;

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
        User user = new User();
        user.setEmail("bob@gmail.com");
        savedUser = userRepo.save(user);
    }

}
