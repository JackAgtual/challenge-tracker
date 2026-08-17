package com.agtual.challengetracker.testsupport;

import org.springframework.security.oauth2.jwt.Jwt;

import com.agtual.challengetracker.entity.User;

public class TestUtils {
    public static User createuser() {
        User user = new User();
        user.setId(1L);
        return user;
    }

    public static Jwt buildJwt(String subject) {
        return Jwt.withTokenValue("fake-token").subject(subject).header("Bearer token", "1234").build();
    }
}
