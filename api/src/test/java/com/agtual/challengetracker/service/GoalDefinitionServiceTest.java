package com.agtual.challengetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.agtual.challengetracker.dto.request.CreateGoalRequest;
import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ChallengeStatus;
import com.agtual.challengetracker.enums.ResourceType;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.ParticipantRepo;
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
    ParticipantService participantService;

    @Autowired
    GoalDefinitionService goalDefinitionService;

    @Autowired
    GoalDefinitionRepo goalDefinitionRepo;

    @Autowired
    ChallengeRepo challengeRepo;

    @Autowired
    ParticipantRepo participantRepo;

    Participant participant;
    Challenge challenge;

    @BeforeAll
    static void beforeAll() {
        createGoalRequest = new CreateGoalRequest(goalName);
    }

    @BeforeEach
    void beforeEach() {
        Challenge pendingChallenge = TestEntityFactory.validChallenge(savedUser, "75 hard");
        pendingChallenge.setStatus(ChallengeStatus.PENDING);
        challenge = challengeRepo.saveAndFlush(pendingChallenge);

        participant = participantRepo
                .saveAndFlush(TestEntityFactory.validParticipant(savedUser, challenge));

    }

    @Test
    void testCreateGoal() {
        long challengeId = 333L;
        when(participantService.getChallengeParticipationForUserAndChallengeId(savedUser, challengeId))
                .thenReturn(participant);

        GoalDefinition goal = goalDefinitionService.createGoal(savedUser, challengeId, createGoalRequest);

        assertEquals(1, goalDefinitionRepo.count());
        assertEquals(goalName, goal.getName());
        assertEquals(participant, goal.getParticipant());
    }

    @Test
    void testCreateGoalParticipantNotFound() {
        Long challengeId = 12L;
        when(participantService.getChallengeParticipationForUserAndChallengeId(savedUser, challengeId))
                .thenThrow(new NotFoundException(ResourceType.PARTICIPANT, challengeId));

        assertThrows(NotFoundException.class,
                () -> goalDefinitionService.createGoal(savedUser, challengeId, createGoalRequest));
    }

    @Test
    void testGetGoal() {
        GoalDefinition goal = goalDefinitionRepo
                .saveAndFlush(TestEntityFactory.validGoalDefinition(participant, goalName));

        GoalDefinition res = goalDefinitionService.getGoal(savedUser, goal.getId());

        assertEquals(goal, res);
    }

    @Test
    void testGetGoalInvalidGoal() {
        GoalDefinition goal = goalDefinitionRepo
                .saveAndFlush(TestEntityFactory.validGoalDefinition(participant, goalName));
        assertThrows(NotFoundException.class, () -> goalDefinitionService.getGoal(savedUser, goal.getId() + 1));
    }

    @Test
    void testGetGoalInvalidUser() {
        User userWhoDoesntOwnGoal = saveRandomUser();
        GoalDefinition goal = goalDefinitionRepo
                .saveAndFlush(TestEntityFactory.validGoalDefinition(participant, goalName));
        assertThrows(NotFoundException.class,
                () -> goalDefinitionService.getGoal(userWhoDoesntOwnGoal, goal.getId()));
    }

    @Test
    void testCantCreateGoalWhenChallengeIsInProgress() {
        Challenge inProgressChallenge = TestEntityFactory.validChallenge(savedUser, "75 hard 2");
        inProgressChallenge.setStatus(ChallengeStatus.IN_PROGRESS);
        Challenge challenge = challengeRepo.saveAndFlush(inProgressChallenge);
        participant = participantRepo
                .saveAndFlush(TestEntityFactory.validParticipant(savedUser, challenge));

        long challengeId = 333L;
        when(participantService.getChallengeParticipationForUserAndChallengeId(savedUser, challengeId))
                .thenReturn(participant);

        assertThrows(ForbiddenException.class,
                () -> goalDefinitionService.createGoal(savedUser, challengeId, createGoalRequest));
    }

    @Test
    void testCantCreateGoalWhenChallengeIsComplete() {
        Challenge completedChallenge = TestEntityFactory.validChallenge(savedUser, "75 hard 2");
        completedChallenge.setStatus(ChallengeStatus.COMPLETE);
        Challenge challenge = challengeRepo.saveAndFlush(completedChallenge);
        participant = participantRepo
                .saveAndFlush(TestEntityFactory.validParticipant(savedUser, challenge));

        long challengeId = 333L;
        when(participantService.getChallengeParticipationForUserAndChallengeId(savedUser, challengeId))
                .thenReturn(participant);

        assertThrows(ForbiddenException.class,
                () -> goalDefinitionService.createGoal(savedUser, challengeId, createGoalRequest));
    }

    @Test
    void testGetGoalsForChallenge() {
        GoalDefinition goal1 = goalDefinitionRepo
                .save(TestEntityFactory.validGoalDefinition(participant, "drink water"));
        GoalDefinition goal2 = goalDefinitionRepo.save(TestEntityFactory.validGoalDefinition(participant, "read"));
        GoalDefinition goal3 = goalDefinitionRepo.save(TestEntityFactory.validGoalDefinition(participant, "meditate"));

        List<GoalDefinition> goals = goalDefinitionService.getGoalsForChallenge(savedUser, challenge.getId());

        assertEquals(3, goals.size());
        assertTrue(goals.containsAll(List.of(goal1, goal2, goal3)));
    }
}
