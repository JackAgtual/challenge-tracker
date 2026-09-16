package com.agtual.challengetracker.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.GoalCompletion;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ChallengeStatus;
import com.agtual.challengetracker.enums.ResourceType;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.GoalCompletionRepo;

@Service
@lombok.RequiredArgsConstructor
public class GoalCompletionService {

    private final GoalDefinitionService goalDefinitionService;
    private final GoalCompletionRepo goalCompletionRepo;
    private final Clock clock;

    public GoalCompletion completeGoal(User user, Long challengeId, Long goalDefinitionId, LocalDate date) {
        if (date.isAfter(LocalDate.now(clock))) {
            throw new ForbiddenException(ResourceType.GOAL_DEFINITION, goalDefinitionId,
                    "Can't complete goal in future");
        }

        GoalDefinition goal = goalDefinitionService.getGoal(user, goalDefinitionId);

        Challenge challenge = goal.getParticipant().getChallenge();
        if (challenge.getStatus() != ChallengeStatus.IN_PROGRESS) {
            throw new ForbiddenException("Challenge must be in progres to complete goals");
        }
        if (!challenge.getId().equals(challengeId)) {
            throw new ForbiddenException("Goal definition does not belong to inputted challenge");
        }

        Optional<GoalCompletion> goalCompletionDuplicate = goalCompletionRepo
                .findByGoalDefinitionAndCompletedDate(goal, date);

        if (goalCompletionDuplicate.isPresent()) {
            throw new ForbiddenException(ResourceType.GOAL_COMPLETION, goalCompletionDuplicate.get().getId(),
                    "Duplicate goal completion");
        }

        GoalCompletion completion = new GoalCompletion();
        completion.setGoalDefinition(goal);
        completion.setCompletedDate(date);
        return goalCompletionRepo.save(completion);
    }

    public void uncompleteGoal(User user, Long challengeId, Long goalDefinitionId, Long goalCompletionId) {
        GoalCompletion goalToDelete = goalCompletionRepo.findById(goalCompletionId)
                .orElseThrow(() -> new NotFoundException(ResourceType.GOAL_COMPLETION, goalCompletionId));

        GoalDefinition goalDefinition = goalToDelete.getGoalDefinition();
        if (!goalDefinition.getId().equals(goalDefinitionId)) {
            throw new ForbiddenException("Goal completion to remove does not belong to inputted goal definition");
        }

        Participant participant = goalDefinition.getParticipant();
        if (participant.getUser() != user) {
            // Throw not found for authorization error
            throw new NotFoundException(ResourceType.GOAL_COMPLETION, goalCompletionId);
        }

        Challenge challenge = participant.getChallenge();

        if (!challenge.getId().equals(challengeId)) {
            throw new ForbiddenException(ResourceType.GOAL_COMPLETION, goalCompletionId,
                    "Goal completion does not belong to inputted challenge");
        }

        if (!challenge.getStatus().equals(ChallengeStatus.IN_PROGRESS)) {
            throw new ForbiddenException("Only allowed to uncomplete goals if challenge is in progress");
        }

        goalCompletionRepo.deleteById(goalCompletionId);
    }

    public List<GoalCompletion> getAllGoalCompletionsForChallenge(User user, Long challengeId) {
        return goalCompletionRepo
                .findByGoalDefinition_Participant_Challenge_IdAndGoalDefinition_Participant_UserOrderByCompletedDateDesc(
                        challengeId, user);
    }
}
