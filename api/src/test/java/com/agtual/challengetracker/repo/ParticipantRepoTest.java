package com.agtual.challengetracker.repo;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.testutil.PostgresJpaBaseTest;
import com.agtual.challengetracker.testutil.TestEntityFactory;

public class ParticipantRepoTest extends PostgresJpaBaseTest {

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
