package com.agtual.challengetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.entity.GoalCompletion;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ChallengeStatus;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.ParticipantRepo;
import com.agtual.challengetracker.repo.ChallengeRepo;
import com.agtual.challengetracker.repo.GoalCompletionRepo;
import com.agtual.challengetracker.repo.GoalDefinitionRepo;
import com.agtual.challengetracker.testutil.MockUserBaseTest;
import com.agtual.challengetracker.testutil.TestEntityFactory;

@DataJpaTest
@Import(GoalCompletionService.class)
public class GoalCompletionServiceTest extends MockUserBaseTest {

    static LocalDate completedDate = LocalDate.of(2026, 6, 5);
    static LocalDate dateInFuture = LocalDate.of(2026, 6, 6);

    @MockitoBean
    GoalDefinitionService goalDefinitionService;

    @Autowired
    GoalDefinitionRepo goalDefinitionRepo;

    @Autowired
    ChallengeRepo challengeRepo;

    @Autowired
    ParticipantRepo participantRepo;

    @Autowired
    GoalCompletionService goalCompletionService;

    @Autowired
    GoalCompletionRepo goalCompletionRepo;

    @TestConfiguration
    static class ClockTestConfig {
        @Bean
        @Primary
        Clock clock() {
            return Clock.fixed(LocalDate.of(2026, 6, 5).atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneId.of("UTC"));
        }
    }

    GoalDefinition goal1;
    GoalDefinition goal2;

    @BeforeEach
    void beforeEach() {
        Challenge validChallenge = TestEntityFactory.validChallenge(savedUser, "75 hard");
        validChallenge.setStatus(ChallengeStatus.IN_PROGRESS);
        Challenge challenge = challengeRepo.save(validChallenge);

        createGoalsFromChallenge(challenge);
    }

    @Test
    void testCompleteGoal() {
        GoalCompletion completedGoal = goalCompletionService.completeGoal(savedUser, goal1.getId(), completedDate);
        assertEquals(completedGoal, goalCompletionRepo.findById(completedGoal.getId()).get());
        assertEquals(completedDate, completedGoal.getCompletedDate());
        assertEquals(goal1, completedGoal.getGoalDefinition());
    }

    @Test
    void testCanCompleteMultipleGoalsSameDay() {
        goalCompletionService.completeGoal(savedUser, goal1.getId(), completedDate);
        goalCompletionService.completeGoal(savedUser, goal2.getId(), completedDate);
        assertEquals(2, goalCompletionRepo.count());
    }

    @Test
    void testCantCompleteGoalInFuture() {
        assertThrows(ForbiddenException.class,
                () -> goalCompletionService.completeGoal(savedUser, goal1.getId(), dateInFuture));
    }

    @Test
    void testCantCompleteOtherUsersGoal() {
        User nonOwner = saveRandomUser();

        when(goalDefinitionService.getGoal(
                argThat(arg -> !arg.equals(savedUser)),
                argThat(arg -> arg.equals(goal1.getId()))))
                .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
                () -> goalCompletionService.completeGoal(nonOwner, goal1.getId(), completedDate));
    }

    @Test
    void testCantCompleteInvalidGoal() {
        when(goalDefinitionService.getGoal(
                argThat(arg -> arg.equals(savedUser)),
                argThat(arg -> !arg.equals(goal1.getId()))))
                .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
                () -> goalCompletionService.completeGoal(savedUser, goal2.getId(), completedDate));
    }

    @Test
    void testCantCompleteGoalIfChallengeIsPending() {
        Challenge pendingChallenge = TestEntityFactory.validChallenge(savedUser, "75 hard 2");
        pendingChallenge.setStatus(ChallengeStatus.PENDING);
        Challenge challenge = challengeRepo.save(pendingChallenge);
        createGoalsFromChallenge(challenge);

        assertThrows(ForbiddenException.class,
                () -> goalCompletionService.completeGoal(savedUser, goal1.getId(), completedDate));
    }

    @Test
    void testCantCompleteGoalIfChallengeIsComplete() {
        Challenge pendingChallenge = TestEntityFactory.validChallenge(savedUser, "75 hard 2");
        pendingChallenge.setStatus(ChallengeStatus.COMPLETE);
        Challenge challenge = challengeRepo.save(pendingChallenge);
        createGoalsFromChallenge(challenge);

        assertThrows(ForbiddenException.class,
                () -> goalCompletionService.completeGoal(savedUser, goal1.getId(), completedDate));
    }

    @Nested
    class UncompleteGoal {
        GoalCompletion goalCompletion1;
        GoalCompletion goalCompletion2;

        @MockitoBean
        Clock clock; // needed for application context

        @BeforeEach
        void beforeEach() {
            goalCompletion1 = goalCompletionRepo.saveAndFlush(TestEntityFactory.validGoalCompletion(goal1,
                    completedDate));
            goalCompletion2 = goalCompletionRepo.saveAndFlush(TestEntityFactory.validGoalCompletion(goal2,
                    completedDate));
        }

        @Test
        void testUncompleteGoal() {
            goalCompletionService.uncompleteGoal(savedUser, goalCompletion1.getId());
            assertEquals(1, goalCompletionRepo.count());

            // test another uncomplete
            goalCompletionService.uncompleteGoal(savedUser, goalCompletion2.getId());
            assertEquals(0, goalCompletionRepo.count());
        }

        @Test
        void testCantUncompleteOtherUsersGoal() {
            User otherUser = saveRandomUser();

            assertThrows(NotFoundException.class,
                    () -> goalCompletionService.uncompleteGoal(otherUser,
                            goalCompletion1.getId()));
        }

        @Test
        void testCantUncompleteInvalidGoal() {
            assertThrows(NotFoundException.class,
                    () -> goalCompletionService.uncompleteGoal(savedUser, 999999L));

        }
    }

    private void createGoalsFromChallenge(Challenge challenge) {
        Participant participant = participantRepo
                .save(TestEntityFactory.validParticipant(savedUser, challenge));

        goal1 = goalDefinitionRepo.save(TestEntityFactory.validGoalDefinition(participant, "drink water"));
        goal2 = goalDefinitionRepo.save(TestEntityFactory.validGoalDefinition(participant, "run 1 mile"));

        when(goalDefinitionService.getGoal(savedUser, goal1.getId())).thenReturn(goal1);
        when(goalDefinitionService.getGoal(savedUser, goal2.getId())).thenReturn(goal2);
    }

}
