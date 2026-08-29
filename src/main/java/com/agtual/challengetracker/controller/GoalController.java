package com.agtual.challengetracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agtual.challengetracker.controller.resolver.CurrentUser;
import com.agtual.challengetracker.dto.request.CompleteGoalRequest;
import com.agtual.challengetracker.dto.request.CreateGoalRequest;
import com.agtual.challengetracker.dto.response.GoalCompletionResponse;
import com.agtual.challengetracker.dto.response.GoalDefinitionResponse;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.service.GoalCompletionService;
import com.agtual.challengetracker.service.GoalDefinitionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/challenges/{challengeId}/goals")
@lombok.RequiredArgsConstructor
public class GoalController {

    private final GoalDefinitionService goalDefinitionService;
    private final GoalCompletionService goalCompletionService;

    @GetMapping
    List<GoalDefinitionResponse> getAllGoalsForChallenge(@CurrentUser User user, @PathVariable Long challengeId) {
        return goalDefinitionService.getGoalsForChallenge(user, challengeId)
                .stream()
                .map(goal -> GoalDefinitionResponse.from(goal)).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createGoalDefinition(@CurrentUser User user, @PathVariable Long challengeId,
            @Valid @RequestBody CreateGoalRequest createGoalRequest) {
        goalDefinitionService.createGoal(user, challengeId, createGoalRequest);
    }

    @PostMapping("/{goalDefinitionId}/completions")
    @ResponseStatus(HttpStatus.CREATED)
    public void recordGoalCompletion(@CurrentUser User user, @PathVariable Long challengeId,
            @Valid CompleteGoalRequest completeGoalRequest) {
        goalCompletionService.completeGoal(user, challengeId, completeGoalRequest.date());
    }

    @GetMapping("/{goalDefinitionId}/completions")
    public List<GoalCompletionResponse> getAllGoalCompletionsForChallenge(@CurrentUser User user,
            @PathVariable Long challengeId) {
        return goalCompletionService.getAllGoalCompletionsForChallenge(user, challengeId)
                .stream()
                .map(completion -> GoalCompletionResponse.from(completion))
                .toList();
    }

    @DeleteMapping("/{goalDefinitionId}/completions/{goalCompletionId}")
    public void deleteExistingGoalCompletion(@CurrentUser User user, @PathVariable Long challengeId,
            @PathVariable Long goalCompletionId) {
        goalCompletionService.uncompleteGoal(user, challengeId, goalCompletionId);
    }

}
