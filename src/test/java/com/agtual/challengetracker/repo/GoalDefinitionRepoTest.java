package com.agtual.challengetracker.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.ChallengeParticipant;
import com.agtual.challengetracker.testutil.MockUserBaseTest;
import com.agtual.challengetracker.testutil.TestEntityFactory;

@DataJpaTest
public class GoalDefinitionRepoTest extends MockUserBaseTest {

    @Autowired
    GoalDefinitionRepo goalDefinitionRepo;

    @Autowired
    ChallengeParticipantRepo challengeParticipantRepo;

    @Autowired
    ChallengeRepo challengeRepo;

    @Test
    void testChallengeParticipantGoaldDefinitionsMustBeUnique() {
        Challenge challenge = challengeRepo.saveAndFlush(TestEntityFactory.validChallenge(savedUser, "my challenge"));
        ChallengeParticipant participant = challengeParticipantRepo
                .saveAndFlush(TestEntityFactory.validChallengeParticipant(savedUser, challenge));

        String goalName = "goal";
        goalDefinitionRepo.saveAndFlush(TestEntityFactory.validGoalDefinition(participant, goalName));

        // save goal with different name
        goalDefinitionRepo.saveAndFlush(TestEntityFactory.validGoalDefinition(participant, goalName + "different"));

        assertEquals(2, goalDefinitionRepo.count());
        assertThrows(DataIntegrityViolationException.class,
                () -> goalDefinitionRepo.saveAndFlush(TestEntityFactory.validGoalDefinition(participant, goalName)));
    }
}
