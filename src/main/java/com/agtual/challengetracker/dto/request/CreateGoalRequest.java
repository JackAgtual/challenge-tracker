package com.agtual.challengetracker.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateGoalRequest(@NotBlank String name) {

}
