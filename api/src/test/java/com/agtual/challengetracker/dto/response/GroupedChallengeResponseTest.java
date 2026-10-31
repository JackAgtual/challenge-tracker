package com.agtual.challengetracker.dto.response;

import static com.agtual.challengetracker.enums.ChallengeStatus.COMPLETE;
import static com.agtual.challengetracker.enums.ChallengeStatus.IN_PROGRESS;
import static com.agtual.challengetracker.enums.ChallengeStatus.PENDING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ChallengeStatus;
import com.agtual.challengetracker.testutil.TestEntityFactory;

public class GroupedChallengeResponseTest {

    static User user = TestEntityFactory.validUser();
    static Clock clock = TestEntityFactory.getFixedClock(2026, 6, 12);

    @Test
    void testFrom() {

        Challenge pending1 = createSpyChallenge(ChallengeStatus.PENDING);

        Challenge pending2 = createSpyChallenge(ChallengeStatus.PENDING);

        Challenge inProgress1 = createSpyChallenge(ChallengeStatus.IN_PROGRESS);

        Challenge complete1 = createSpyChallenge(ChallengeStatus.COMPLETE);

        Challenge complete2 = createSpyChallenge(ChallengeStatus.COMPLETE);

        Challenge complete3 = createSpyChallenge(ChallengeStatus.COMPLETE);

        GroupedChallengeResponse res = GroupedChallengeResponse
                .from(List.of(pending1, pending2, inProgress1, complete1, complete2, complete3), clock);

        List<ChallengeResponse> pending = res.pending();
        List<ChallengeResponse> inProgress = res.inProgress();
        List<ChallengeResponse> complete = res.complete();

        assertEquals(2, pending.size());
        assertTrue(pending.stream().map(c -> c.name()).toList()
                .containsAll(List.of(pending1.getName(), pending2.getName())));
        assertTrue(pending.stream().map(c -> c.status()).allMatch(el -> el.equals(PENDING)));

        assertEquals(1, inProgress.size());
        assertTrue(inProgress.stream().map(c -> c.name()).toList().contains(inProgress1.getName()));
        assertTrue(inProgress.stream().map(c -> c.status()).allMatch(el -> el.equals(IN_PROGRESS)));

        assertEquals(3, complete.size());
        assertTrue(complete.stream().map(c -> c.name()).toList()
                .containsAll(List.of(complete1.getName(), complete2.getName(), complete3.getName())));
        assertTrue(complete.stream().map(c -> c.status()).allMatch(el -> el.equals(COMPLETE)));
    }

    private Challenge createSpyChallenge(ChallengeStatus status) {
        Challenge challenge = spy(TestEntityFactory.validChallenge(user, UUID.randomUUID().toString()));
        // Set same completion percentage no matter the status because it doesn't impact
        // this test
        doReturn(0.5).when(challenge).getCompletionPercentage(clock);
        challenge.setStatus(status);
        return challenge;
    }
}
