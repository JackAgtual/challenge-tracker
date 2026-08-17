package com.agtual.challengetracker.service;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.repo.UserRepo;
import com.agtual.challengetracker.testsupport.TestUtils;

public abstract class MockUserBaseTest {
    static Jwt jwt = TestUtils.buildJwt("subject");

    User savedUser;

    @Autowired
    UserRepo userRepo;

    @MockitoBean
    UserService userService;

    @BeforeEach
    void setupUserServiceMocks() {
        User user = new User();
        user.setEmail("bob@gmail.com");
        savedUser = userRepo.save(user);
        when(userService.getValidUser(jwt)).thenReturn(savedUser);
    }

}
