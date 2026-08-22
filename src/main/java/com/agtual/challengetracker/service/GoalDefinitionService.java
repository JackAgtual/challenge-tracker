package com.agtual.challengetracker.service;

import org.springframework.stereotype.Service;

import com.agtual.challengetracker.dto.request.CreateGoalRequest;
import com.agtual.challengetracker.entity.ChallengeParticipant;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ChallengeStatus;
import com.agtual.challengetracker.enums.ResourceType;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.GoalDefinitionRepo;

@Service
@lombok.RequiredArgsConstructor
public class GoalDefinitionService {

    private final GoalDefinitionRepo goalDefinitionRepo;
    private final ChallengeParticipantService challengeParticipantService;

    public GoalDefinition createGoal(User user, Long challengeId, CreateGoalRequest createGoalRequest) {
        ChallengeParticipant participant = challengeParticipantService
                .getChallengeParticipationForUserAndChallengeId(user, challengeId);

        if (participant.getChallenge().getStatus() != ChallengeStatus.PENDING) {
            throw new ForbiddenException("Can only create goal when challenge is pending");
        }

        GoalDefinition goalDefinition = new GoalDefinition();
        goalDefinition.setParticipant(participant);
        goalDefinition.setName(createGoalRequest.name());
        return goalDefinitionRepo.save(goalDefinition);
    }

    GoalDefinition getGoal(User user, Long goalId) {
        GoalDefinition goal = goalDefinitionRepo.findById(goalId)
                .orElseThrow(() -> new NotFoundException(ResourceType.GOAL_DEFINITION, goalId));

        if (goal.getParticipant().getParticipant() != user) {
            // Throw not found for authorization error
            throw new NotFoundException(ResourceType.GOAL_DEFINITION, goalId);
        }

        return goal;
    }
}
