package com.agtual.challengetracker.dto.response;

import jakarta.validation.constraints.NotNull;

public record BooleanResponse(@NotNull boolean value) {

}
