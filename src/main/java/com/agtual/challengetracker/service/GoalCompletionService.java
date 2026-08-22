package com.agtual.challengetracker.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.agtual.challengetracker.entity.GoalCompletion;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ResourceType;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.repo.GoalCompletionRepo;

@Service
@lombok.RequiredArgsConstructor
public class GoalCompletionService {

    private final GoalDefinitionService goalDefinitionService;
    private final GoalCompletionRepo goalCompletionRepo;
    private final Clock clock;

    public GoalCompletion completeGoal(User user, Long goalDefinitionId, LocalDate date) {
        if (date.isAfter(LocalDate.now(clock))) {
            throw new ForbiddenException(ResourceType.GOAL_DEFINITION, goalDefinitionId,
                    "Can't complete goal in future");
        }

        GoalDefinition goal = goalDefinitionService.getGoal(user, goalDefinitionId);

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
}
