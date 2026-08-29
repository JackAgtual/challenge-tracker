package com.agtual.challengetracker.dto.response;

import com.agtual.challengetracker.entity.GoalDefinition;

public record GoalDefinitionResponse(Long id, String name) {
    public static GoalDefinitionResponse from(GoalDefinition goalDefinition) {
        return new GoalDefinitionResponse(goalDefinition.getId(), goalDefinition.getName());
    }
}
