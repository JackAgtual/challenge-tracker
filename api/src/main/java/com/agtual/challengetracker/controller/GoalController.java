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
import com.agtual.challengetracker.dto.response.GoalDefinitionResponse;
import com.agtual.challengetracker.dto.response.GoalTableResponse;
import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.GoalCompletion;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.service.ChallengeService;
import com.agtual.challengetracker.service.GoalCompletionService;
import com.agtual.challengetracker.service.GoalDefinitionService;
import com.agtual.challengetracker.service.ParticipantService;
import com.agtual.challengetracker.util.GridUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/challenges/{challengeId}")
@lombok.RequiredArgsConstructor
public class GoalController {

    private final GoalDefinitionService goalDefinitionService;
    private final GoalCompletionService goalCompletionService;
    private final ChallengeService challengeService;
    private final ParticipantService participantService;

    @GetMapping("/participants/{participantId}/goals")
    List<GoalDefinitionResponse> getAllGoalsForChallenge(@CurrentUser User user, @PathVariable Long challengeId,
            @PathVariable Long participantId) {
        Participant participant = participantService.userGetsParticipantInfo(user, participantId);

        return goalDefinitionService.getGoalsForChallenge(participant.getUser(), challengeId)
                .stream()
                .map(goal -> GoalDefinitionResponse.from(goal)).toList();
    }

    @PostMapping("/goals")
    @ResponseStatus(HttpStatus.CREATED)
    void createGoalDefinition(@CurrentUser User user, @PathVariable Long challengeId,
            @Valid @RequestBody CreateGoalRequest createGoalRequest) {
        goalDefinitionService.createGoal(user, challengeId, createGoalRequest);
    }

    @DeleteMapping("/goals/{goalDefinitionId}")
    public void deleteGoalDefinition(@CurrentUser User user, @PathVariable Long challengeId,
            @PathVariable Long goalDefinitionId) {
        goalDefinitionService.deleteGoal(user, challengeId, goalDefinitionId);
    }

    @PostMapping("/goals/{goalDefinitionId}/completions")
    @ResponseStatus(HttpStatus.CREATED)
    public void recordGoalCompletion(@CurrentUser User user, @PathVariable Long challengeId,
            @PathVariable Long goalDefinitionId,
            @Valid @RequestBody CompleteGoalRequest completeGoalRequest) {
        goalCompletionService.completeGoal(user, challengeId, goalDefinitionId, completeGoalRequest.date());
    }

    @GetMapping("/participants/{participantId}/goals/completions")
    public GoalTableResponse getAllGoalCompletionsForChallenge(@CurrentUser User user,
            @PathVariable Long challengeId, @PathVariable Long participantId) {
        Challenge challenge = challengeService.getChallenge(user, challengeId);

        Participant participant = participantService.userGetsParticipantInfo(user, participantId);

        List<GoalDefinition> goalDefinitions = goalDefinitionService.getGoalsForChallenge(
                participant.getUser(), challenge.getId());
        List<GoalCompletion> goalCompletions = goalCompletionService.getAllGoalCompletionsForChallenge(
                participant.getUser(), challenge.getId());

        return GridUtil.createGoalCompletionTable(challenge, goalDefinitions, goalCompletions);
    }

    @DeleteMapping("/goals/{goalDefinitionId}/completions/{goalCompletionId}")
    public void deleteExistingGoalCompletion(@CurrentUser User user, @PathVariable Long challengeId,
            @PathVariable Long goalDefinitionId,
            @PathVariable Long goalCompletionId) {
        goalCompletionService.uncompleteGoal(user, challengeId, goalDefinitionId, goalCompletionId);
    }

}
