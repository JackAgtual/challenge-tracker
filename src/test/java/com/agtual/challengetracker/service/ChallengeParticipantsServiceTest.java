package com.agtual.challengetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.ChallengeParticipant;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.InviteStatus;
import com.agtual.challengetracker.repo.ChallengeParticipantRepo;
import com.agtual.challengetracker.repo.ChallengeRepo;

@DataJpaTest
@Import(ChallengeParticipantService.class)
public class ChallengeParticipantsServiceTest extends MockUserBaseTest {

    private Challenge savedChallenge1;

    @Autowired
    ChallengeParticipantService challengeParticipantService;

    @Autowired
    ChallengeParticipantRepo challengeParticipantRepo;

    @Autowired
    ChallengeRepo challengeRepo;

    @BeforeEach
    void beforeEach() {
        Challenge challenge1 = new Challenge();
        challenge1.setOwner(savedUser);
        challenge1.setName("my challenge");
        savedChallenge1 = challengeRepo.save(challenge1);

    }

    @Test
    void testAddOwnerToChallenge() {
        ChallengeParticipant owner = challengeParticipantService.addOwnerToChallenge(savedUser, savedChallenge1);

        assertEquals(savedChallenge1, owner.getChallenge());
        assertEquals(savedUser, owner.getParticipant());
        assertEquals(InviteStatus.ACCEPTED, owner.getInviteStatus());
        assertEquals(false, owner.isReady());

        ChallengeParticipant participantInRepo = challengeParticipantRepo.findById(owner.getId()).get();
        assertEquals(owner, participantInRepo);
    }

    @Test
    void testAllJoinedParticipantsAreReadyWhenAllAreReady() {
        saveParticipant(InviteStatus.ACCEPTED, true);
        saveParticipant(InviteStatus.ACCEPTED, true);
        saveParticipant(InviteStatus.ACCEPTED, true);
        saveParticipant(InviteStatus.DECLINED, false);
        saveParticipant(InviteStatus.PENDING, false);

        assertTrue(challengeParticipantService.allJoinedParticipantsAreReady(savedChallenge1));
    }

    @Test
    void testAllJoinedParticipantsAreReadyWhenNotAllAreReady() {
        saveParticipant(InviteStatus.ACCEPTED, true);
        saveParticipant(InviteStatus.ACCEPTED, false);
        saveParticipant(InviteStatus.ACCEPTED, true);
        saveParticipant(InviteStatus.DECLINED, false);
        saveParticipant(InviteStatus.PENDING, false);

        assertFalse(challengeParticipantService.allJoinedParticipantsAreReady(savedChallenge1));
    }

    @Test
    void testIsParticipant() {
        User user1NotInChallenge = saveRandomUser();
        User user2InChallenge = saveRandomUser();
        addUserToChallenge(user2InChallenge, savedChallenge1);

        assertFalse(challengeParticipantService.isParticipant(user1NotInChallenge, savedChallenge1));
        assertTrue(challengeParticipantService.isParticipant(user2InChallenge, savedChallenge1));
    }

    private void saveParticipant(InviteStatus inviteStatus, boolean ready) {
        User userEntity = saveRandomUser();

        ChallengeParticipant participant = new ChallengeParticipant();
        participant.setParticipant(userEntity);
        participant.setChallenge(savedChallenge1);
        participant.setReady(ready);
        participant.setInviteStatus(inviteStatus);
        challengeParticipantRepo.save(participant);
    }

    private void addUserToChallenge(User user, Challenge challenge) {
        ChallengeParticipant participant = new ChallengeParticipant();
        participant.setParticipant(user);
        participant.setChallenge(challenge);
        challengeParticipantRepo.save(participant);
    }
}
