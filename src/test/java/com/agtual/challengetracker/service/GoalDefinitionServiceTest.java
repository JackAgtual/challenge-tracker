package com.agtual.challengetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.agtual.challengetracker.dto.request.CreateGoalRequest;
import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.ChallengeParticipant;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.enums.ResourceType;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.ChallengeParticipantRepo;
import com.agtual.challengetracker.repo.ChallengeRepo;
import com.agtual.challengetracker.repo.GoalDefinitionRepo;
import com.agtual.challengetracker.testutil.MockUserBaseTest;
import com.agtual.challengetracker.testutil.TestEntityFactory;

@DataJpaTest
@Import(GoalDefinitionService.class)
public class GoalDefinitionServiceTest extends MockUserBaseTest {
    static CreateGoalRequest createGoalRequest;
    static String goalName = "drink water";

    @MockitoBean
    ChallengeParticipantService challengeParticipantService;

    @Autowired
    GoalDefinitionService goalDefinitionService;

    @Autowired
    GoalDefinitionRepo goalDefinitionRepo;

    @Autowired
    ChallengeRepo challengeRepo;

    @Autowired
    ChallengeParticipantRepo challengeParticipantRepo;

    ChallengeParticipant participant;

    @BeforeAll
    static void beforeAll() {
        createGoalRequest = new CreateGoalRequest(goalName);
    }

    @BeforeEach
    void beforeEach() {
        Challenge challenge = challengeRepo.save(TestEntityFactory.validChallenge(savedUser, "75 hard"));

        participant = challengeParticipantRepo
                .save(TestEntityFactory.validChallengeParticipant(savedUser, challenge));

    }

    @Test
    void testCreateGoal() {
        long challengeId = 333L;
        when(challengeParticipantService.getChallengeParticipationForUserAndChallengeId(savedUser, challengeId))
                .thenReturn(participant);

        GoalDefinition goal = goalDefinitionService.createGoal(savedUser, challengeId, createGoalRequest);

        assertEquals(1, goalDefinitionRepo.count());
        assertEquals(goalName, goal.getName());
        assertEquals(participant, goal.getParticipant());
    }

    @Test
    void testCreateGoalChallengeParticipantNotFound() {
        Long challengeId = 12L;
        when(challengeParticipantService.getChallengeParticipationForUserAndChallengeId(savedUser, challengeId))
                .thenThrow(new NotFoundException(ResourceType.CHALLENGE_PARTICIPANT, challengeId));

        assertThrows(NotFoundException.class,
                () -> goalDefinitionService.createGoal(savedUser, challengeId, createGoalRequest));
    }
}
