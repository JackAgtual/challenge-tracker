package com.agtual.challengetracker.dto.response;

import com.agtual.challengetracker.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record ValidUserResponse(@NotEmpty Long id, @NotBlank String firstName, @NotBlank String lastName,
        @NotBlank String username, String avatarSrc) {
    public static ValidUserResponse from(User user) {
        return new ValidUserResponse(user.getId(), user.getFirstName(),
                user.getLastName(), user.getUsername(), user.getAvatarSrc());
    }
}