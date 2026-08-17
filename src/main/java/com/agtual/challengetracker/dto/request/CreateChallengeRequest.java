package com.agtual.challengetracker.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateChallengeRequest(@NotBlank String name, @Min(1) int durationDays) {
}
