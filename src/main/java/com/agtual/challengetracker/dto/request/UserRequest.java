package com.agtual.challengetracker.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(@Email String email, @NotBlank String firstName,
                @NotBlank String lastName) {
}
