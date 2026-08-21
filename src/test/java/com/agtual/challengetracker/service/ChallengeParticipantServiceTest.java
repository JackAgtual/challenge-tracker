package com.agtual.challengetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.ChallengeParticipant;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.InviteStatus;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.ChallengeParticipantRepo;
import com.agtual.challengetracker.repo.ChallengeRepo;
import com.agtual.challengetracker.testutil.MockUserBaseTest;
import com.agtual.challengetracker.testutil.TestEntityFactory;

@DataJpaTest
@Import(ChallengeParticipantService.class)
public class ChallengeParticipantServiceTest extends MockUserBaseTest {

    private Challenge savedChallenge1;

    @Autowired
    ChallengeParticipantService challengeParticipantService;

    @Autowired
    ChallengeParticipantRepo challengeParticipantRepo;

    @Autowired
    ChallengeRepo challengeRepo;

    @BeforeEach
    void beforeEach() {
        savedChallenge1 = saveChallengeWithOwner(savedUser);
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

    @Test
    void testGetAllChallengesForUser() {
        User otherUser = saveRandomUser();

        Challenge challenge1 = saveChallengeWithOwner(otherUser);
        Challenge challenge2 = saveChallengeWithOwner(savedUser);
        Challenge challenge3 = saveChallengeWithOwner(savedUser);
        Challenge challenge4 = saveChallengeWithOwner(savedUser);
        Challenge challenge5 = saveChallengeWithOwner(savedUser);

        // user is owner of challenge
        ChallengeParticipant participant1 = TestEntityFactory.validChallengeParticipant(savedUser, challenge1);
        participant1.setInviteStatus(InviteStatus.ACCEPTED);
        participant1 = challengeParticipantRepo.save(participant1);

        // user accepted invite to challenge
        ChallengeParticipant participant2 = TestEntityFactory.validChallengeParticipant(savedUser, challenge2);
        participant2.setInviteStatus(InviteStatus.ACCEPTED);
        participant2 = challengeParticipantRepo.save(participant2);

        // user has not responded to challenge invite
        // will still be included in response
        ChallengeParticipant participant3 = TestEntityFactory.validChallengeParticipant(savedUser, challenge3);
        participant3.setInviteStatus(InviteStatus.PENDING);
        participant3 = challengeParticipantRepo.save(participant3);

        // user declined invite to challenge, should not be included in response
        ChallengeParticipant participant4 = TestEntityFactory.validChallengeParticipant(savedUser, challenge4);
        participant4.setInviteStatus(InviteStatus.DECLINED);
        participant4 = challengeParticipantRepo.save(participant4);

        // participant is a different user, should not be included in response
        ChallengeParticipant participant5 = TestEntityFactory.validChallengeParticipant(otherUser, challenge5);
        participant5.setInviteStatus(InviteStatus.DECLINED);
        participant5 = challengeParticipantRepo.save(participant5);

        List<ChallengeParticipant> participations = challengeParticipantService
                .getAllChallengeParticipationsForUser(savedUser);
        assertEquals(5, challengeParticipantRepo.count());
        assertEquals(3, participations.size());
        assertTrue(participations.containsAll(List.of(participant1, participant2, participant3)));
    }

    @Test
    void testGetFromChallengeIdAndParticipant() {
        ChallengeParticipant participant = challengeParticipantRepo
                .save(TestEntityFactory.validChallengeParticipant(savedUser, savedChallenge1));

        ChallengeParticipant participantRes = challengeParticipantService
                .getChallengeParticipationForUserAndChallengeId(savedUser, savedChallenge1.getId());
        assertEquals(participant, participantRes);
    }

    @Test
    void testGetFromChallengeIdAndParticipantNotFound() {
        challengeParticipantRepo
                .save(TestEntityFactory.validChallengeParticipant(savedUser, savedChallenge1));

        assertThrows(NotFoundException.class,
                () -> challengeParticipantService.getChallengeParticipationForUserAndChallengeId(savedUser, 99999L));

        User nonParticipant = saveRandomUser();
        assertThrows(NotFoundException.class,
                () -> challengeParticipantService.getChallengeParticipationForUserAndChallengeId(nonParticipant,
                        savedChallenge1.getId()));

    }

    private void saveParticipant(InviteStatus inviteStatus, boolean ready) {
        User userEntity = saveRandomUser();

        ChallengeParticipant participant = TestEntityFactory.validChallengeParticipant(userEntity, savedChallenge1);
        participant.setReady(ready);
        participant.setInviteStatus(inviteStatus);
        challengeParticipantRepo.save(participant);
    }

    private void addUserToChallenge(User user, Challenge challenge) {
        ChallengeParticipant participant = TestEntityFactory.validChallengeParticipant(user, challenge);
        challengeParticipantRepo.save(participant);
    }

    private Challenge saveChallengeWithOwner(User owner) {
        Challenge challenge = TestEntityFactory.validChallenge(owner, UUID.randomUUID().toString());
        return challengeRepo.save(challenge);
    }
}
