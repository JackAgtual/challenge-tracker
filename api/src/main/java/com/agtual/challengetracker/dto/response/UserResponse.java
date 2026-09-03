package com.agtual.challengetracker.dto.response;

import com.agtual.challengetracker.entity.User;

public record UserResponse(String authSubject, String email, String firstName, String lastName) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getAuthSubject(), user.getEmail(), user.getFirstName(),
                user.getLastName());
    }
}
