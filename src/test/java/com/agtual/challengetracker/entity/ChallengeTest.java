package com.agtual.challengetracker.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.agtual.challengetracker.dto.request.ModifyChallengeRequest;
import com.agtual.challengetracker.enums.ChallengeStatus;

public class ChallengeTest {

    @Test
    void testReadyToStart() {
        Challenge challenge = createReadyChallenge();
        assertTrue(challenge.isReadyToStart());

        challenge.setDurationDays(null);
        assertFalse(challenge.isReadyToStart());

        challenge = createReadyChallenge();
        challenge.setName(null);
        assertFalse(challenge.isReadyToStart());

        challenge = createReadyChallenge();
        challenge.setStatus(ChallengeStatus.IN_PROGRESS);
        assertFalse(challenge.isReadyToStart());

        challenge = createReadyChallenge();
        challenge.setStatus(ChallengeStatus.COMPLETE);
        assertFalse(challenge.isReadyToStart());

        // test combination of invalid values
        challenge = createReadyChallenge();
        challenge.setStatus(ChallengeStatus.IN_PROGRESS);
        challenge.setDurationDays(null);
        assertFalse(challenge.isReadyToStart());
    }

    @Test
    void testUpdate() {
        Challenge challenge = new Challenge();
        ModifyChallengeRequest mod1 = new ModifyChallengeRequest("new name", 20);

        challenge.update(mod1);

        assertEquals(mod1.name(), challenge.getName());
        assertEquals(mod1.durationDays(), challenge.getDurationDays());

        ModifyChallengeRequest mod2 = new ModifyChallengeRequest("final name", null);

        challenge.update(mod2);
        assertEquals(mod2.name(), challenge.getName());
        assertNull(challenge.getDurationDays());
    }

    @Test
    void testChallegeStatusDefaultsToPending() {
        Challenge challenge = new Challenge();
        assertEquals(ChallengeStatus.PENDING, challenge.getStatus());
    }

    @Test
    void testIsConfigurable() {
        Challenge challenge = new Challenge();
        challenge.setStatus(ChallengeStatus.PENDING);
        assertTrue(challenge.isConfigurable());

        challenge.setStatus(ChallengeStatus.IN_PROGRESS);
        assertFalse(challenge.isConfigurable());

        challenge.setStatus(ChallengeStatus.COMPLETE);
        assertFalse(challenge.isConfigurable());
    }

    private Challenge createReadyChallenge() {
        Challenge challenge = new Challenge();
        challenge.setStatus(ChallengeStatus.PENDING);
        challenge.setName("c1");
        challenge.setDurationDays(30);
        return challenge;
    }
}
