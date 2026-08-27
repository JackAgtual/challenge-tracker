package com.agtual.challengetracker;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

@TestConfiguration
public class TestConfg {
    @Bean
    JwtDecoder jwtDecoder() {
        return token -> {
            throw new JwtException("Test JWT decoder");
        };
    }
}
