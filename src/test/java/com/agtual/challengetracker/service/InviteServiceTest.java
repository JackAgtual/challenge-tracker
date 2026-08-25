package com.agtual.challengetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.Invite;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.InviteStatus;
import com.agtual.challengetracker.exception.ForbiddenException;
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
}
