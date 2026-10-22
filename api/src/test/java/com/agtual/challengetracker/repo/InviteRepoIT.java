package com.agtual.challengetracker.repo;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.Invite;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.InviteStatus;
import com.agtual.challengetracker.testutil.PostgresJpaBaseTest;
import com.agtual.challengetracker.testutil.TestEntityFactory;

public class InviteRepoIT extends PostgresJpaBaseTest {

    @Autowired
    ChallengeRepo challengeRepo;

    @Autowired
    InviteRepo inviteRepo;

    @Test
    void testUserCanOnlyBeInvitedToChallengeOnce() {
        Challenge challenge = challengeRepo.save(TestEntityFactory.validChallenge(savedUser, "75 medium"));

        User userToInvite = saveRandomUser();

        inviteRepo.save(TestEntityFactory.validInvite(challenge, savedUser, userToInvite));

        assertThrows(DataIntegrityViolationException.class,
                () -> inviteRepo.save(TestEntityFactory.validInvite(challenge, savedUser, userToInvite)));
    }

    @Test
    void testInviteMustHaveChallenge() {
        User userToInvite = saveRandomUser();

        Invite invite = new Invite();
        invite.setInviteSender(savedUser);
        invite.setInvitedUser(userToInvite);
        invite.setStatus(InviteStatus.PENDING);

        assertThrows(DataIntegrityViolationException.class, () -> inviteRepo.save(invite));
    }

    @Test
    void testInviteMustHaveInviteSender() {
        Challenge challenge = challengeRepo.save(TestEntityFactory.validChallenge(savedUser, "75 medium"));
        User userToInvite = saveRandomUser();

        Invite invite = new Invite();
        invite.setChallenge(challenge);
        invite.setInvitedUser(userToInvite);
        invite.setStatus(InviteStatus.PENDING);

        assertThrows(DataIntegrityViolationException.class, () -> inviteRepo.save(invite));
    }

    @Test
    void testInviteMustHaveInvitedUser() {
        Challenge challenge = challengeRepo.save(TestEntityFactory.validChallenge(savedUser, "75 medium"));

        Invite invite = new Invite();
        invite.setChallenge(challenge);
        invite.setInviteSender(savedUser);
        invite.setStatus(InviteStatus.PENDING);

        assertThrows(DataIntegrityViolationException.class, () -> inviteRepo.save(invite));
    }

    @Test
    void testInviteMustHaveStatus() {
        Challenge challenge = challengeRepo.save(TestEntityFactory.validChallenge(savedUser, "75 medium"));
        User userToInvite = saveRandomUser();

        Invite invite = new Invite();
        invite.setChallenge(challenge);
        invite.setInviteSender(savedUser);
        invite.setInvitedUser(userToInvite);
        invite.setStatus(null);

        assertThrows(DataIntegrityViolationException.class, () -> inviteRepo.save(invite));
    }
}
