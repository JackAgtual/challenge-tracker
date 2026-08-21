package com.agtual.challengetracker.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.ChallengeParticipant;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.testutil.MockUserBaseTest;
import com.agtual.challengetracker.testutil.TestEntityFactory;

@DataJpaTest
public class GoalCompletionRepoTest extends MockUserBaseTest {
    @Autowired
    GoalDefinitionRepo goalDefinitionRepo;

    @Autowired
    ChallengeParticipantRepo challengeParticipantRepo;

    @Autowired
    ChallengeRepo challengeRepo;

    @Autowired
    GoalCompletionRepo completedGoalRepo;

    @Test
    void testGoalCompletionsCantExistForSameGoalAndSameDate() {
        Challenge challenge = challengeRepo
                .saveAndFlush(TestEntityFactory.validChallenge(savedUser, "my challenge"));
        ChallengeParticipant participant = challengeParticipantRepo
                .saveAndFlush(TestEntityFactory.validChallengeParticipant(savedUser, challenge));

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
}
