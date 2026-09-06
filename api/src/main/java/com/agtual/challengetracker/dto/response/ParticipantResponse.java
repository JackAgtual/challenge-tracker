package com.agtual.challengetracker.dto.response;

import com.agtual.challengetracker.entity.Participant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParticipantResponse(@NotBlank String username, @NotNull boolean ready) {
    public static ParticipantResponse from(Participant participant) {
        return new ParticipantResponse(participant.getUser().getUsername(), participant.isReady());
    }
}
