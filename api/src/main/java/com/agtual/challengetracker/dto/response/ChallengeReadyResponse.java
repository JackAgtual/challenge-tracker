package com.agtual.challengetracker.dto.response;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record ChallengeReadyResponse(@NotNull boolean ready, List<String> reasons) {

}
