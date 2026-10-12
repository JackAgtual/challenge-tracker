package com.agtual.challengetracker.dto.response;

import com.agtual.challengetracker.entity.Participant;

import jakarta.validation.constraints.NotNull;

public record ReadyResponse(@NotNull Boolean ready, @NotNull Boolean eligibleToSetReady) {
    public static ReadyResponse from(Participant participant) {
        return new ReadyResponse(participant.isReady(), participant.isEligibleToSetReady());
    }
}
