package com.agtual.challengetracker.repo;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.testutil.MockUserBaseTest;
import com.agtual.challengetracker.testutil.TestEntityFactory;

@DataJpaTest
public class ParticipantRepoTest extends MockUserBaseTest {

    @Autowired
    ChallengeRepo challengeRepo;

    @Autowired
    ParticipantRepo participantRepo;

    @Test
    void testSameParticipantCanOnlyParticipateInChallengeOnce() {
        Challenge challenge = TestEntityFactory.validChallenge(savedUser, "my challenge");
        challengeRepo.saveAndFlush(challenge);

        Participant participant = TestEntityFactory.validParticipant(savedUser, challenge);
        participant.setReady(true);

        participantRepo.saveAndFlush(participant);

        Participant sameParticipant = TestEntityFactory.validParticipant(savedUser, challenge);

        assertThrows(DataIntegrityViolationException.class, () -> participantRepo.saveAndFlush(
                sameParticipant));
    }

}
