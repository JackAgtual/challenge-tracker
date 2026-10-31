package com.agtual.challengetracker.dto.response;

import java.time.Clock;
import java.time.LocalDate;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.enums.ChallengeStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChallengeResponse(@NotNull Long id, @NotBlank String name, LocalDate starDate,
        @NotNull Integer durationDays, @NotNull ChallengeStatus status, @NotNull Double completionPercentage,
        @NotNull UserResponse owner) {
    public static ChallengeResponse from(Challenge challenge, Clock clock) {
        return new ChallengeResponse(challenge.getId(), challenge.getName(), challenge.getStartDate(),
                challenge.getDurationDays(), challenge.getStatus(), challenge.getCompletionPercentage(clock),
                UserResponse.from(challenge.getOwner()));
    }

    public static ChallengeResponse from(Participant participant, Clock clock) {
        return from(participant.getChallenge(), clock);
    }
}
