package com.agtual.challengetracker.dto.response;

import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParticipantResponse(@NotNull Long id, @NotBlank String username, @NotBlank String firstName,
        @NotBlank String lastName, @NotNull boolean ready) {
    public static ParticipantResponse from(Participant participant) {
        User user = participant.getUser();
        return new ParticipantResponse(participant.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
                participant.isReady());
    }
}
