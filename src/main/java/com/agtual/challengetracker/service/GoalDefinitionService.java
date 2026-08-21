package com.agtual.challengetracker.service;

import org.springframework.stereotype.Service;

import com.agtual.challengetracker.dto.request.CreateGoalRequest;
import com.agtual.challengetracker.entity.ChallengeParticipant;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.repo.GoalDefinitionRepo;

@Service
@lombok.RequiredArgsConstructor
public class GoalDefinitionService {

    private final GoalDefinitionRepo goalDefinitionRepo;
    private final ChallengeParticipantService challengeParticipantService;

    public GoalDefinition createGoal(User user, Long challengeId, CreateGoalRequest createGoalRequest) {
        ChallengeParticipant participant = challengeParticipantService
                .getChallengeParticipationForUserAndChallengeId(user, challengeId);

        GoalDefinition goalDefinition = new GoalDefinition();
        goalDefinition.setParticipant(participant);
        goalDefinition.setName(createGoalRequest.name());
        return goalDefinitionRepo.save(goalDefinition);
    }
}
