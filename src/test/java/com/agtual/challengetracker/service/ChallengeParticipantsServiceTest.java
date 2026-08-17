package com.agtual.challengetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.ChallengeParticipant;
import com.agtual.challengetracker.enums.InviteStatus;
import com.agtual.challengetracker.repo.ChallengeParticipantRepo;
import com.agtual.challengetracker.repo.ChallengeRepo;

@DataJpaTest
@Import(ChallengeParticipantService.class)
public class ChallengeParticipantsServiceTest extends MockUserBaseTest {

    private Challenge savedChallenge;

    @Autowired
    ChallengeParticipantService challengeParticipantService;

    @Autowired
    ChallengeParticipantRepo challengeParticipantRepo;

    @Autowired
    ChallengeRepo challengeRepo;

    @BeforeEach
    void beforeEach() {
        Challenge challenge = new Challenge();
        challenge.setOwner(savedUser);
        challenge.setName("my challenge");
        savedChallenge = challengeRepo.save(challenge);
    }

    @Test
    void testAddOwnerToChallenge() {
        ChallengeParticipant owner = challengeParticipantService.addOwnerToChallenge(savedUser, savedChallenge);

        assertEquals(savedChallenge, owner.getChallenge());
        assertEquals(savedUser, owner.getParticipant());
        assertEquals(InviteStatus.ACCEPTED, owner.getInviteStatus());
        assertEquals(false, owner.isReady());

        ChallengeParticipant participantInRepo = challengeParticipantRepo.findById(owner.getId()).get();
        assertEquals(owner, participantInRepo);
    }
}
