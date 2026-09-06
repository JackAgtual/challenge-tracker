package com.agtual.challengetracker.dto.response;

import jakarta.validation.constraints.NotNull;

public record ReadyRequest(@NotNull boolean ready) {

}
