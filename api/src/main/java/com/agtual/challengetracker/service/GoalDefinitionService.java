package com.agtual.challengetracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.agtual.challengetracker.dto.request.CreateGoalRequest;
import com.agtual.challengetracker.entity.Participant;
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
    private final ParticipantService participantService;

    public GoalDefinition createGoal(User user, Long challengeId, CreateGoalRequest createGoalRequest) {
        Participant participant = participantService
                .getChallengeParticipationForUserAndChallengeId(user, challengeId);

        if (participant.getChallenge().getStatus() != ChallengeStatus.PENDING) {
            throw new ForbiddenException("Can only create goal when challenge is pending");
        }

        String trimmedName = createGoalRequest.name().trim();

        if (goalDefinitionRepo.existsByNameIgnoreCaseAndParticipant_Id(
                trimmedName,
                participant.getId())) {
            throw new ForbiddenException("Goal definition already exists in challenge");
        }

        GoalDefinition goalDefinition = new GoalDefinition();
        goalDefinition.setParticipant(participant);
        goalDefinition.setName(trimmedName);
        return goalDefinitionRepo.save(goalDefinition);
    }

    public void deleteGoal(User user, Long challengeId, Long goalId) {
        GoalDefinition goal = getGoal(user, goalId);

        if (!goal.getParticipant().getChallenge().getId().equals(challengeId)) {
            throw new ForbiddenException(ResourceType.GOAL_DEFINITION, goalId, "Goal belongs to different challenge");
        }

        goalDefinitionRepo.delete(goal);
    }

    public GoalDefinition getGoal(User user, Long goalId) {
        GoalDefinition goal = goalDefinitionRepo.findById(goalId)
                .orElseThrow(() -> new NotFoundException(ResourceType.GOAL_DEFINITION, goalId));

        if (goal.getParticipant().getUser() != user) {
            // Throw not found for authorization error
            throw new NotFoundException(ResourceType.GOAL_DEFINITION, goalId);
        }

        return goal;
    }

    public List<GoalDefinition> getGoalsForChallenge(User user, Long challengeId) {
        return goalDefinitionRepo.findByParticipant_UserAndParticipant_Challenge_Id(user, challengeId);
    }
}
