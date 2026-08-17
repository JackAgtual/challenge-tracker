package com.agtual.challengetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.agtual.challengetracker.dto.request.CreateChallengeRequest;
import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.exception.AlreadyExistsException;
import com.agtual.challengetracker.repo.ChallengeRepo;

@DataJpaTest
@Import(ChallengeService.class)
public class ChallengeServiceTest extends MockUserBaseTest {

    static CreateChallengeRequest createChallengeRequest = new CreateChallengeRequest("my challenge", 30);

    @Autowired
    ChallengeService challengeService;

    @Autowired
    ChallengeRepo challengeRepo;

    @MockitoBean
    ChallengeParticipantService challengeParticipantService;

    @Test
    void testCreateChallenge() {
        Challenge challenge = challengeService.createChallenge(jwt, createChallengeRequest);

        assertEquals(createChallengeRequest.name(), challenge.getName());
        assertEquals(createChallengeRequest.durationDays(), challenge.getDurationDays());

        Challenge challengeFromRepo = challengeRepo.findById(challenge.getId()).get();
        assertEquals(challenge, challengeFromRepo);
    }

    @Test
    void testCreateChallengeThrowsExceptionIfUserAlreadyOwnsChallengeWithSameName() {
        Challenge existingChallenge = new Challenge();
        existingChallenge.setName(createChallengeRequest.name());
        existingChallenge.setOwner(savedUser);

        challengeRepo.save(existingChallenge);

        assertThrows(AlreadyExistsException.class, () -> challengeService.createChallenge(jwt, createChallengeRequest));
    }

    @Test
    void testCreateChallengeAddsOwnerAsChallengeParticipant() {
        Challenge challenge = challengeService.createChallenge(jwt, createChallengeRequest);
        verify(challengeParticipantService).addOwnerToChallenge(savedUser, challenge);
    }

}
