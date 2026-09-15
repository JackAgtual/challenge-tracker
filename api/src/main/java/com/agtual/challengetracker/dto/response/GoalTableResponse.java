package com.agtual.challengetracker.dto.response;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record GoalTableResponse(@NotNull List<GoalDefinitionResponse> goalDefinitionResponse,
        @NotNull List<GoalCompletionRowResponse> goalCompletionRowResponses) {

}
