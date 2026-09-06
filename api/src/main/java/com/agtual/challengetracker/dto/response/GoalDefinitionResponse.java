package com.agtual.challengetracker.dto.response;

import com.agtual.challengetracker.entity.GoalDefinition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GoalDefinitionResponse(@NotNull Long id, @NotBlank String name) {
    public static GoalDefinitionResponse from(GoalDefinition goalDefinition) {
        return new GoalDefinitionResponse(goalDefinition.getId(), goalDefinition.getName());
    }
}
