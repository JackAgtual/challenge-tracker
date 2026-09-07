package com.agtual.challengetracker.dto.response;

import static com.agtual.challengetracker.enums.ChallengeStatus.COMPLETE;
import static com.agtual.challengetracker.enums.ChallengeStatus.IN_PROGRESS;
import static com.agtual.challengetracker.enums.ChallengeStatus.PENDING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.testutil.TestEntityFactory;

public class GroupedChallengeResponseTest {

    @Test
    void testFrom() {
        User user = TestEntityFactory.validUser();

        Challenge pending1 = TestEntityFactory.validChallenge(user, "c1");
        pending1.setStatus(PENDING);

        Challenge pending2 = TestEntityFactory.validChallenge(user, "c2");
        pending2.setStatus(PENDING);

        Challenge inProgress1 = TestEntityFactory.validChallenge(user, "c3");
        inProgress1.setStatus(IN_PROGRESS);

        Challenge complete1 = TestEntityFactory.validChallenge(user, "c4");
        complete1.setStatus(COMPLETE);

        Challenge complete2 = TestEntityFactory.validChallenge(user, "c5");
        complete2.setStatus(COMPLETE);

        Challenge complete3 = TestEntityFactory.validChallenge(user, "c6");
        complete3.setStatus(COMPLETE);

        GroupedChallengeResponse res = GroupedChallengeResponse
                .from(List.of(pending1, pending2, inProgress1, complete1, complete2, complete3));

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
}
