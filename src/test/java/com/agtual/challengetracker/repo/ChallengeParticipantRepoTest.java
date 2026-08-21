package com.agtual.challengetracker.repo;

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
public class ChallengeParticipantRepoTest extends MockUserBaseTest {

    @Autowired
    ChallengeRepo challengeRepo;

    @Autowired
    ChallengeParticipantRepo challengeParticipantRepo;

    @Test
    void testSameParticipantCanOnlyParticipateInChallengeOnce() {
        Challenge challenge = TestEntityFactory.validChallenge(savedUser, "my challenge");
        challengeRepo.saveAndFlush(challenge);

        ChallengeParticipant participant = TestEntityFactory.validChallengeParticipant(savedUser, challenge);
        participant.setReady(true);

        challengeParticipantRepo.saveAndFlush(participant);

        ChallengeParticipant sameParticipant = TestEntityFactory.validChallengeParticipant(savedUser, challenge);

        assertThrows(DataIntegrityViolationException.class, () -> challengeParticipantRepo.saveAndFlush(
                sameParticipant));
    }

}
