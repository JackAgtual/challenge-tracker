package com.agtual.challengetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.Invite;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ChallengeStatus;
import com.agtual.challengetracker.enums.InviteStatus;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.ChallengeRepo;
import com.agtual.challengetracker.repo.InviteRepo;
import com.agtual.challengetracker.testutil.MockUserBaseTest;
import com.agtual.challengetracker.testutil.TestEntityFactory;

@DataJpaTest
@Import(InviteService.class)
public class InviteServiceTest extends MockUserBaseTest {
    @Autowired
    InviteService inviteService;

    @Autowired
    InviteRepo inviteRepo;

    @Autowired
    ChallengeRepo challengeRepo;

    @MockitoBean
    ChallengeService challengeService;

    @MockitoBean
    ParticipantService participantService;

    Challenge challenge;

    @BeforeEach
    void beforeEach() {
        challenge = challengeRepo.save(TestEntityFactory.validChallenge(savedUser, "75 hard"));
        when(challengeService.getChallengeFromOwner(challenge.getId(), savedUser)).thenReturn(challenge);
    }

    @Test
    void testInviteToChallenge() {
        User userToInvite = saveRandomUser();

        Invite invite = inviteService.inviteToChallenge(savedUser, challenge.getId(), userToInvite);

        assertEquals(challenge, invite.getChallenge());
        assertEquals(savedUser, invite.getInviteSender());
        assertEquals(userToInvite, invite.getInvitedUser());
        assertEquals(InviteStatus.PENDING, invite.getStatus());

        assertEquals(invite, inviteRepo.findById(invite.getId()).get());
    }

    @Test
    void testCantSendMultipleInvitesToUser() {
        User alreadyInvitedUser = saveRandomUser();

        inviteRepo.save(TestEntityFactory.validInvite(challenge, alreadyInvitedUser, alreadyInvitedUser));

        assertThrows(ForbiddenException.class,
                () -> inviteService.inviteToChallenge(savedUser, challenge.getId(), alreadyInvitedUser));
    }

    @Test
    void testAcceptInvite() {
        User userToInvite = saveRandomUser();
        Invite invite = inviteRepo.save(TestEntityFactory.validInvite(challenge, savedUser, userToInvite));

        inviteService.acceptInvite(userToInvite, invite.getId());

        verify(participantService).addUserToChallenge(userToInvite, challenge);

        invite = inviteRepo.findById(invite.getId()).get();
        assertEquals(challenge, invite.getChallenge());
        assertEquals(savedUser, invite.getInviteSender());
        assertEquals(userToInvite, invite.getInvitedUser());
        assertEquals(InviteStatus.ACCEPTED, invite.getStatus());
    }

    @Test
    void testAcceptInviteInvalidInviteId() {
        User userToInvite = saveRandomUser();
        Invite invite = inviteRepo.save(TestEntityFactory.validInvite(challenge, savedUser, userToInvite));

        assertThrows(NotFoundException.class, () -> inviteService.acceptInvite(userToInvite, invite.getId() + 99L));
    }

    @Test
    void testAcceptInviteInvalidUser() {
        User userToInvite = saveRandomUser();
        User userNotInvited = saveRandomUser();
        Invite invite = inviteRepo.save(TestEntityFactory.validInvite(challenge, savedUser, userToInvite));
        assertThrows(NotFoundException.class, () -> inviteService.acceptInvite(userNotInvited, invite.getId()));
    }

    @Test
    void testCanOnlyAcceptInviteWhenChallengeIsPending() {
        // In progress challenge
        challenge.setStatus(ChallengeStatus.IN_PROGRESS);
        challenge = challengeRepo.save(challenge);

        User userToInvite = saveRandomUser();
        Invite invite = inviteRepo
                .save(TestEntityFactory.validInvite(challenge, userToInvite, userToInvite));

        assertThrows(ForbiddenException.class,
                () -> inviteService.acceptInvite(userToInvite, invite.getId()));

        // Complete challenge
        challenge.setStatus(ChallengeStatus.COMPLETE);
        challenge = challengeRepo.save(challenge);

        invite.setChallenge(challenge);
        Invite inviteWithCompleteChallenge = inviteRepo.save(invite);

        assertThrows(ForbiddenException.class,
                () -> inviteService.acceptInvite(userToInvite, inviteWithCompleteChallenge.getId()));
    }

    @Test
    void testDeclineInvite() {
        User userToInvite = saveRandomUser();
        Invite invite = inviteRepo.save(TestEntityFactory.validInvite(challenge, savedUser, userToInvite));

        inviteService.declineInvite(userToInvite, invite.getId());

        invite = inviteRepo.findById(invite.getId()).get();
        assertEquals(challenge, invite.getChallenge());
        assertEquals(savedUser, invite.getInviteSender());
        assertEquals(userToInvite, invite.getInvitedUser());
        assertEquals(InviteStatus.DECLINED, invite.getStatus());

    }

    @Test
    void testDeclineInviteInvalidInviteId() {
        User userToInvite = saveRandomUser();
        Invite invite = inviteRepo.save(TestEntityFactory.validInvite(challenge, savedUser, userToInvite));

        assertThrows(NotFoundException.class, () -> inviteService.declineInvite(userToInvite, invite.getId() + 99L));
    }

    @Test
    void testDeclineInviteInvalidUser() {
        User userToInvite = saveRandomUser();
        User userNotInvited = saveRandomUser();
        Invite invite = inviteRepo.save(TestEntityFactory.validInvite(challenge, savedUser, userToInvite));
        assertThrows(NotFoundException.class, () -> inviteService.declineInvite(userNotInvited, invite.getId()));
    }

    @Test
    void testCanOnlyDeclineInviteWhenChallengeIsPending() {
        // In progress challenge
        challenge.setStatus(ChallengeStatus.IN_PROGRESS);
        challenge = challengeRepo.save(challenge);

        User userToInvite = saveRandomUser();
        Invite invite = inviteRepo
                .save(TestEntityFactory.validInvite(challenge, userToInvite, userToInvite));

        assertThrows(ForbiddenException.class,
                () -> inviteService.declineInvite(userToInvite, invite.getId()));

        // Complete challenge
        challenge.setStatus(ChallengeStatus.COMPLETE);
        challenge = challengeRepo.save(challenge);

        invite.setChallenge(challenge);
        Invite inviteWithCompleteChallenge = inviteRepo.save(invite);

        assertThrows(ForbiddenException.class,
                () -> inviteService.declineInvite(userToInvite, inviteWithCompleteChallenge.getId()));
    }

    @Test
    void testGetPendingInvites() {
        Challenge c1 = challengeRepo.save(TestEntityFactory.validChallenge(savedUser, "c1"));
        Challenge c2 = challengeRepo.save(TestEntityFactory.validChallenge(savedUser, "c2"));
        Challenge c3 = challengeRepo.save(TestEntityFactory.validChallenge(savedUser, "c3"));
        Challenge c4 = challengeRepo.save(TestEntityFactory.validChallenge(savedUser, "c4"));

        User user1 = saveRandomUser();
        User user2 = saveRandomUser();

        Invite invite1 = inviteRepo.save(TestEntityFactory.validInvite(c1, savedUser, user1));
        Invite invite2 = inviteRepo.save(TestEntityFactory.validInvite(c1, savedUser, user2));
        Invite invite3 = inviteRepo.save(TestEntityFactory.validInvite(c2, savedUser, user1));
        Invite invite4 = inviteRepo.save(TestEntityFactory.validInvite(c2, savedUser, user2));
        Invite preInvite3 = TestEntityFactory.validInvite(c3, savedUser, user1);
        preInvite3.setStatus(InviteStatus.ACCEPTED);
        inviteRepo.save(preInvite3);
        Invite preInvite6 = TestEntityFactory.validInvite(c4, savedUser, user1);
        preInvite6.setStatus(InviteStatus.DECLINED);
        inviteRepo.save(preInvite6);

        List<Invite> user1PendingInvites = inviteService.getPendingInvites(user1);
        assertEquals(2, user1PendingInvites.size());
        assertTrue(user1PendingInvites.containsAll(List.of(invite1, invite3)));

        List<Invite> user2PendingInvites = inviteService.getPendingInvites(user2);
        assertEquals(2, user2PendingInvites.size());
        assertTrue(user2PendingInvites.containsAll(List.of(invite2, invite4)));

    }
}
