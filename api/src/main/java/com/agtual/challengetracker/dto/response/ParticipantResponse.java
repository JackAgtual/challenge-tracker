package com.agtual.challengetracker.dto.response;

import java.time.LocalDate;
import java.time.ZoneId;

import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.entity.User;

import jakarta.validation.constraints.NotNull;

public record ParticipantResponse(@NotNull Long id, @NotNull ValidUserResponse user, @NotNull boolean ready,
        @NotNull LocalDate joinedAt,
        @NotNull boolean isChallengeOwner) {
    public static ParticipantResponse from(Participant participant) {
        User user = participant.getUser();
        boolean isChallengeOwner = participant.getChallenge().getOwner().getId() == user.getId();
        return new ParticipantResponse(participant.getId(), ValidUserResponse.from(user),
                participant.isReady(), LocalDate.ofInstant(participant.getJoinedAt(), ZoneId.systemDefault()),
                isChallengeOwner);
    }
}
