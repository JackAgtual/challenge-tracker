package com.agtual.challengetracker.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.agtual.challengetracker.enums.ChallengeStatus;

public class InviteTest {

    @Test
    void testRecipientCanRespondOnlyIfChallengeIsPending() {
        Invite invite = createInviteFromChallengeStatus(ChallengeStatus.PENDING);
        assertTrue(invite.recipientCanRespond());

        invite = createInviteFromChallengeStatus(ChallengeStatus.COMPLETE);
        assertFalse(invite.recipientCanRespond());

        invite = createInviteFromChallengeStatus(ChallengeStatus.IN_PROGRESS);
        assertFalse(invite.recipientCanRespond());

    }

    private Invite createInviteFromChallengeStatus(ChallengeStatus challengeStatus) {
        Challenge challenge = new Challenge();
        Invite invite = new Invite();

        challenge.setStatus(challengeStatus);
        invite.setChallenge(challenge);
        return invite;
    }
}
