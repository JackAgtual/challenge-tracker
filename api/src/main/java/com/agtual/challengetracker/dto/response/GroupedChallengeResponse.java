package com.agtual.challengetracker.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.enums.ChallengeStatus;

import jakarta.validation.constraints.NotNull;

public record GroupedChallengeResponse(@NotNull List<ChallengeResponse> inProgress,
        @NotNull List<ChallengeResponse> pending,
        @NotNull List<ChallengeResponse> complete) {

    public static GroupedChallengeResponse from(List<Challenge> challenges) {
        List<ChallengeResponse> inProgresss = new ArrayList<>();
        List<ChallengeResponse> pending = new ArrayList<>();
        List<ChallengeResponse> complete = new ArrayList<>();
        challenges.forEach(challenge -> {
            switch (challenge.getStatus()) {
                case ChallengeStatus.IN_PROGRESS:
                    inProgresss.add(ChallengeResponse.from(challenge));
                    break;
                case ChallengeStatus.PENDING:
                    pending.add(ChallengeResponse.from(challenge));
                    break;
                case ChallengeStatus.COMPLETE:
                    complete.add(ChallengeResponse.from(challenge));
                    break;
            }
        });
        return new GroupedChallengeResponse(inProgresss, pending, complete);
    }
}
