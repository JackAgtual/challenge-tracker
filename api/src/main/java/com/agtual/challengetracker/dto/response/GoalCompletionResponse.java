package com.agtual.challengetracker.dto.response;

import java.time.LocalDate;

import com.agtual.challengetracker.entity.GoalCompletion;

import jakarta.validation.constraints.NotNull;

public record GoalCompletionResponse(@NotNull Long id, @NotNull Long goalDefinitionId,
        @NotNull LocalDate completedDate) {
    public static GoalCompletionResponse from(GoalCompletion goalCompletion) {
        return new GoalCompletionResponse(
                goalCompletion.getId(),
                goalCompletion.getGoalDefinition().getId(),
                goalCompletion.getCompletedDate());
    }
}
