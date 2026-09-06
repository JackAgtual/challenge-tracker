package com.agtual.challengetracker.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChallengeNameResponse(@NotNull Long id, @NotBlank String name) {

}
