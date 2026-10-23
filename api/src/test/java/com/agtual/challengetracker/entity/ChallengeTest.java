package com.agtual.challengetracker.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.agtual.challengetracker.dto.request.ChallengeDataRequest;
import com.agtual.challengetracker.enums.ChallengeStatus;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.testutil.TestEntityFactory;

public class ChallengeTest {

    @Test
    void testReadyToStart() {
        Challenge challenge = createReadyToStartChallenge();
        assertTrue(challenge.isReadyToStart());

        challenge.setDurationDays(null);
        assertFalse(challenge.isReadyToStart());

        challenge = createReadyToStartChallenge();
        challenge.setName(null);
        assertFalse(challenge.isReadyToStart());

        challenge = createReadyToStartChallenge();
        challenge.setStatus(ChallengeStatus.IN_PROGRESS);
        assertFalse(challenge.isReadyToStart());

        challenge = createReadyToStartChallenge();
        challenge.setStatus(ChallengeStatus.COMPLETE);
        assertFalse(challenge.isReadyToStart());

        // test combination of invalid values
        challenge = createReadyToStartChallenge();
        challenge.setStatus(ChallengeStatus.IN_PROGRESS);
        challenge.setDurationDays(null);
        assertFalse(challenge.isReadyToStart());
    }

    @Test
    void testUpdate() {
        Challenge challenge = new Challenge();
        ChallengeDataRequest mod1 = new ChallengeDataRequest("new name", 20);

        challenge.update(mod1);

        assertEquals(mod1.name(), challenge.getName());
        assertEquals(mod1.durationDays(), challenge.getDurationDays());
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

    @Test
    void testGetMostRecentDateInProgressChallenge() {
        Challenge challenge = new Challenge();
        challenge.setStatus(ChallengeStatus.IN_PROGRESS);
        challenge.setStartDate(LocalDate.now().minusDays(10));

        assertEquals(LocalDate.now(), challenge.getMostRecentDate());
    }

    @Test
    void testGetMostRecentDateCompleteChallenge() {
        Challenge challenge = new Challenge();
        challenge.setStatus(ChallengeStatus.COMPLETE);
        challenge.setStartDate(LocalDate.of(2025, 6, 5));
        challenge.setDurationDays(15);

        assertEquals(LocalDate.of(2025, 6, 19), challenge.getMostRecentDate());
    }

    @Test
    void testGetMostRecentDateInvalidState() {
        Challenge challenge = new Challenge();
        challenge.setStatus(ChallengeStatus.PENDING);

        assertThrows(ForbiddenException.class, () -> challenge.getMostRecentDate());
    }

    @Test
    void testIsReadyToComplete() {
        Challenge challenge = new Challenge();
        challenge.setStatus(ChallengeStatus.IN_PROGRESS);
        challenge.setStartDate(LocalDate.of(2026, 1, 1));
        challenge.setDurationDays(10);

        Clock clock = TestEntityFactory.getFixedClock(2026, 1, 11);

        assertTrue(challenge.isReadyToComplete(clock));
    }

    @Test
    void testIsReadyToCompleteLastDayOfChallenge() {
        Challenge challenge = new Challenge();
        challenge.setStatus(ChallengeStatus.IN_PROGRESS);
        challenge.setStartDate(LocalDate.of(2026, 1, 1));
        challenge.setDurationDays(10);

        Clock clock = TestEntityFactory.getFixedClock(2026, 1, 10);

        assertFalse(challenge.isReadyToComplete(clock));
    }

    @Test
    void testIsReadyToCompleteChallengeMustBeInProgress() {
        Challenge pendingChallenge = new Challenge();
        pendingChallenge.setStatus(ChallengeStatus.PENDING);
        pendingChallenge.setStartDate(LocalDate.of(2026, 1, 1));
        pendingChallenge.setDurationDays(10);

        Challenge completeChallenge = new Challenge();
        completeChallenge.setStatus(ChallengeStatus.COMPLETE);
        completeChallenge.setStartDate(LocalDate.of(2026, 1, 1));
        completeChallenge.setDurationDays(10);

        Clock clock = TestEntityFactory.getFixedClock(2026, 1, 15);

        assertFalse(pendingChallenge.isReadyToComplete(clock));
        assertFalse(completeChallenge.isReadyToComplete(clock));
    }

    @Test
    void testIsReadyToCOmpleteWorksWithNullStartDate() {
        Challenge challenge = new Challenge();
        challenge.setStatus(ChallengeStatus.IN_PROGRESS);
        challenge.setDurationDays(10);

        Clock clock = TestEntityFactory.getFixedClock(2026, 1, 11);

        assertFalse(challenge.isReadyToComplete(clock));
    }

    @Test
    void testIsReadyToCOmpleteWorksWithNullDuration() {
        Challenge challenge = new Challenge();
        challenge.setStatus(ChallengeStatus.IN_PROGRESS);
        challenge.setStartDate(LocalDate.of(2026, 1, 1));

        Clock clock = TestEntityFactory.getFixedClock(2026, 1, 11);

        assertFalse(challenge.isReadyToComplete(clock));
    }

    private Challenge createReadyToStartChallenge() {
        Challenge challenge = new Challenge();
        challenge.setStatus(ChallengeStatus.PENDING);
        challenge.setName("c1");
        challenge.setDurationDays(30);
        return challenge;
    }

}
