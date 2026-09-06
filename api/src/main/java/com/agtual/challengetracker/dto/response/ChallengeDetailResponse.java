package com.agtual.challengetracker.dto.response;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record ChallengeDetailResponse(@NotNull ChallengeResponse challenge,
        @NotNull List<ParticipantResponse> participants) {

}
