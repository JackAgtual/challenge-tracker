package com.agtual.challengetracker.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.testutil.MockUserBaseTest;
import com.agtual.challengetracker.testutil.TestEntityFactory;

@DataJpaTest
public class ChallengeRepoTest extends MockUserBaseTest {

    @Autowired
    ChallengeRepo repo;

    @Test
    void testOwnerCantHaveChallengesWithSameName() {
        String challengeName = "c1";

        Challenge challenge1 = TestEntityFactory.validChallenge(savedUser, challengeName);
        challenge1.setOwner(savedUser);
        challenge1.setName(challengeName);

        Challenge challenge2 = TestEntityFactory.validChallenge(savedUser, challengeName);

        repo.saveAndFlush(challenge1);
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(challenge2));
    }

    @Test
    void testOwnerCanHaveMultipleChallengesWithDifferentNames() {
        Challenge challenge1 = TestEntityFactory.validChallenge(savedUser, "c1");

        Challenge challenge2 = TestEntityFactory.validChallenge(savedUser, "c2");

        repo.saveAndFlush(challenge1);
        repo.saveAndFlush(challenge2);

        assertEquals(2, repo.count());
    }

    @Test
    void testChallengeMustHaveOwner() {
        Challenge challenge = new Challenge();
        challenge.setName("c1");
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(challenge));
    }

    @Test
    void testChallengeMustHaveName() {
        Challenge challenge = new Challenge();
        challenge.setOwner(savedUser);
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(challenge));
    }

    @Test
    void testChallengeMustHaveStatus() {
        Challenge challenge = new Challenge();
        challenge.setOwner(savedUser);
        challenge.setName("challenge");
        challenge.setStatus(null);
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(challenge));
    }
}
