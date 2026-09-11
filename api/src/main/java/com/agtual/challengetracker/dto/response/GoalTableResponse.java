package com.agtual.challengetracker.dto.response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.agtual.challengetracker.entity.GoalCompletion;
import com.agtual.challengetracker.entity.GoalDefinition;

import jakarta.validation.constraints.NotNull;

public record GoalTableResponse(@NotNull List<GoalDefinitionResponse> goalDefinitionResponse,
        @NotNull List<GoalCompletionRowResponse> goalCompletionRowResponses) {

    public static GoalTableResponse from(List<GoalDefinition> goalDefinitions, List<GoalCompletion> goalCompletions) {

        // Start with all goals completed set to false and populate ones that are true
        Map<Long, Boolean> allFalseGoalCompletions = new HashMap<>(goalDefinitions.size());
        goalDefinitions.forEach(def -> allFalseGoalCompletions.put(def.getId(), Boolean.FALSE));

        // For each date store a list of completions for each goal
        Map<LocalDate, List<GoalCompletion>> dateToGoalCompletions = new LinkedHashMap<>();
        for (GoalCompletion completion : goalCompletions) {
            dateToGoalCompletions
                    .computeIfAbsent(completion.getCompletedDate(), k -> new ArrayList<>())
                    .add(completion);
        }

        // Populate each row (each row is one day)
        List<GoalCompletionRowResponse> rows = new ArrayList<>(dateToGoalCompletions.size());
        for (Map.Entry<LocalDate, List<GoalCompletion>> entry : dateToGoalCompletions.entrySet()) {
            Map<Long, Boolean> goalCompletionsMap = new HashMap<>(allFalseGoalCompletions);
            System.out.println(entry.getKey());
            for (GoalCompletion completion : entry.getValue()) {
                goalCompletionsMap.put(completion.getGoalDefinition().getId(), Boolean.TRUE);
            }
            rows.add(new GoalCompletionRowResponse(entry.getKey(), goalCompletionsMap));
        }

        List<GoalDefinitionResponse> goalDefResponses = goalDefinitions.stream().map(GoalDefinitionResponse::from)
                .toList();

        return new GoalTableResponse(goalDefResponses, rows);
    }
}
