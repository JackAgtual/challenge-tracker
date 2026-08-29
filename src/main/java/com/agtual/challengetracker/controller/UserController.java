package com.agtual.challengetracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agtual.challengetracker.dto.request.CreateUserRequest;
import com.agtual.challengetracker.dto.response.UserResponse;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@lombok.RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateUserRequest createUserRequest) {
        User createdUser = userService.createUser(jwt, createUserRequest);
        return UserResponse.from(createdUser);
    }
}
