package com.agtual.challengetracker.dto.response;

import com.agtual.challengetracker.entity.Participant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParticipantResponse(@NotNull Long participantId, @NotBlank String username, @NotNull boolean ready) {
    public static ParticipantResponse from(Participant participant) {
        return new ParticipantResponse(participant.getId(), participant.getUser().getUsername(), participant.isReady());
    }
}
