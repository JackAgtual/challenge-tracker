package com.agtual.challengetracker.dto.response;

import com.agtual.challengetracker.entity.User;

import jakarta.validation.constraints.NotEmpty;

public record UserNameResponse(@NotEmpty String firstName, @NotEmpty String lastName,
        @NotEmpty String username) {
    public static UserNameResponse from(User user) {
        return new UserNameResponse(user.getFirstName(), user.getLastName(), user.getUsername());
    }
}
