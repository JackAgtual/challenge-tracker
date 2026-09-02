package com.agtual.challengetracker.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserAccountSetupRequest(@NotBlank String firstName,
        @NotBlank String lastName, @NotBlank String username) {

}
