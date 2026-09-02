package com.agtual.challengetracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agtual.challengetracker.controller.resolver.CurrentUser;
import com.agtual.challengetracker.dto.request.CreateUserRequest;
import com.agtual.challengetracker.dto.request.UserAccountSetupRequest;
import com.agtual.challengetracker.dto.response.BooleanResponse;
import com.agtual.challengetracker.dto.response.UserResponse;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.service.UserService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@lombok.RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "200", description = "User already existed, returning existing user")
    })
    public ResponseEntity<UserResponse> createUser(@AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateUserRequest createUserRequest) {
        User createdUser = userService.createUser(jwt, createUserRequest);

        boolean existingUser = createdUser.getUsername() != null;

        HttpStatus status = existingUser ? HttpStatus.OK : HttpStatus.CREATED;
        return ResponseEntity.status(status).body(UserResponse.from(createdUser));
    }

    @GetMapping("/me/is-setup")
    public BooleanResponse isAccountSetup(@CurrentUser User user) {
        return new BooleanResponse(user.isAccountSetup());
    }

    @PatchMapping("/me")
    public void finishUserAccountSetup(@CurrentUser User user,
            @Valid @RequestBody UserAccountSetupRequest accountSetupRequest) {
        userService.finishAccountSetup(user, accountSetupRequest);
    }
}
