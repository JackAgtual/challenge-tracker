package com.agtual.challengetracker.dto.response;

import java.time.LocalDate;
import java.util.Map;

import jakarta.validation.constraints.NotNull;

/**
 * GoalCompletionRowResponse
 * 
 * @param date               of goal completion
 * @param completionsPerGoal map where key is id of goal definition and value is
 *                           boolean if goal was completed on {@date}
 */
public record GoalCompletionRowResponse(@NotNull LocalDate date, @NotNull Map<Long, Boolean> completionsPerGoal) {
}
