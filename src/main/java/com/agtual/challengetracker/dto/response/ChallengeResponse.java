package com.agtual.challengetracker.dto.response;

import java.time.LocalDate;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.enums.ChallengeStatus;

public record ChallengeResponse(Long id, String name, LocalDate starDate, Integer durationDays,
        ChallengeStatus status) {
    public static ChallengeResponse from(Challenge challenge) {
        return new ChallengeResponse(challenge.getId(), challenge.getName(), challenge.getStartDate(),
                challenge.getDurationDays(), challenge.getStatus());
    }
}
