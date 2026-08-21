package com.agtual.challengetracker.testutil;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.repo.UserRepo;

public abstract class MockUserBaseTest {
    protected User savedUser;

    @Autowired
    UserRepo userRepo;

    @BeforeEach
    void saveUserToRepo() {
        savedUser = saveRandomUser();
    }

    protected User saveRandomUser() {
        return userRepo.save(TestEntityFactory.validUser());
    }
}
