package com.agtual.challengetracker.dto.response;

import java.time.LocalDate;

import com.agtual.challengetracker.entity.GoalCompletion;

public record GoalCompletionResponse(Long id, Long goalDefinitionId, LocalDate completedDate) {
    public static GoalCompletionResponse from(GoalCompletion goalCompletion) {
        return new GoalCompletionResponse(
                goalCompletion.getId(),
                goalCompletion.getGoalDefinition().getId(),
                goalCompletion.getCompletedDate());
    }
}
