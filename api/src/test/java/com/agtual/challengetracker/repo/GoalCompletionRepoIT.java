package com.agtual.challengetracker.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.GoalCompletion;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.testutil.PostgresJpaBaseTest;
import com.agtual.challengetracker.testutil.TestEntityFactory;

public class GoalCompletionRepoIT extends PostgresJpaBaseTest {
    @Autowired
    GoalDefinitionRepo goalDefinitionRepo;

    @Autowired
    ParticipantRepo participantRepo;

    @Autowired
    ChallengeRepo challengeRepo;

    @Autowired
    GoalCompletionRepo completedGoalRepo;

    @Test
    void testGoalCompletionsCantExistForSameGoalAndSameDate() {
        Challenge challenge = challengeRepo
                .saveAndFlush(TestEntityFactory.validChallenge(savedUser, "my challenge"));
        Participant participant = participantRepo
                .saveAndFlush(TestEntityFactory.validParticipant(savedUser, challenge));

        GoalDefinition drinkWater = goalDefinitionRepo
                .saveAndFlush(TestEntityFactory.validGoalDefinition(participant, "drink water"));
        GoalDefinition read = goalDefinitionRepo
                .saveAndFlush(TestEntityFactory.validGoalDefinition(participant, "read"));

        // different goal completions can exist for same date
        LocalDate day1 = LocalDate.of(2026, 6, 15);
        completedGoalRepo.saveAndFlush(TestEntityFactory.validGoalCompletion(drinkWater,
                day1));
        completedGoalRepo.saveAndFlush(TestEntityFactory.validGoalCompletion(read,
                day1));
        assertEquals(2, completedGoalRepo.count());

        // same goal completions can exist for different date
        LocalDate day2 = LocalDate.of(2026, 6, 16);
        completedGoalRepo.saveAndFlush(TestEntityFactory.validGoalCompletion(drinkWater,
                day2));
        completedGoalRepo.saveAndFlush(TestEntityFactory.validGoalCompletion(read,
                day2));
        assertEquals(4, completedGoalRepo.count());

        // can't have a repeat goal completion on same date
        assertThrows(DataIntegrityViolationException.class,
                () -> completedGoalRepo.saveAndFlush(TestEntityFactory.validGoalCompletion(read,
                        day2)));
    }

    @Test
    void testGoalCompletionMustHaveGoalDefinition() {
        GoalCompletion completion = new GoalCompletion();
        completion.setCompletedDate(LocalDate.now());

        assertThrows(DataIntegrityViolationException.class, () -> completedGoalRepo.save(completion));
    }

    @Test
    void testGoalCompletionMustHaveCompletedDate() {
        Challenge challenge = challengeRepo
                .saveAndFlush(TestEntityFactory.validChallenge(savedUser, "my challenge"));
        Participant participant = participantRepo
                .saveAndFlush(TestEntityFactory.validParticipant(savedUser, challenge));

        GoalDefinition drinkWater = goalDefinitionRepo
                .saveAndFlush(TestEntityFactory.validGoalDefinition(participant, "drink water"));

        GoalCompletion completion = new GoalCompletion();
        completion.setGoalDefinition(drinkWater);

        assertThrows(DataIntegrityViolationException.class, () -> completedGoalRepo.save(completion));
    }
}
