package com.agtual.challengetracker.repo;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.testutil.MockUserBaseTest;
import com.agtual.challengetracker.testutil.TestEntityFactory;

@DataJpaTest
public class InviteRepoTest extends MockUserBaseTest {

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
}
