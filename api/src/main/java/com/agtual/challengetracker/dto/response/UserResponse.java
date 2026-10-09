package com.agtual.challengetracker.dto.response;

import com.agtual.challengetracker.entity.User;
import jakarta.validation.constraints.NotEmpty;

public record UserResponse(@NotEmpty Long id, @NotEmpty String email, String firstName, String lastName,
        String username) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getFirstName(),
                user.getLastName(), user.getUsername());
    }
}
