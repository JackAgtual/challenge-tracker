package com.agtual.challengetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.agtual.challengetracker.dto.request.CreateChallengeRequest;
import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.exception.AlreadyExistsException;
import com.agtual.challengetracker.exception.NotFoundException;
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
        Challenge challenge = challengeService.createChallenge(savedUser, createChallengeRequest);

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

        assertThrows(AlreadyExistsException.class,
                () -> challengeService.createChallenge(savedUser, createChallengeRequest));
    }

    @Test
    void testCreateChallengeAddsOwnerAsChallengeParticipant() {
        Challenge challenge = challengeService.createChallenge(savedUser, createChallengeRequest);
        verify(challengeParticipantService).addOwnerToChallenge(savedUser, challenge);
    }

    @Test
    void testGetChallenge() {
        Challenge challengeToSave = new Challenge();
        challengeToSave.setOwner(savedUser);
        challengeToSave.setName("my challenge");
        Challenge savedChallenge = challengeRepo.save(challengeToSave);

        when(challengeParticipantService.isParticipant(savedUser, savedChallenge)).thenReturn(true);

        Challenge challengeRes = challengeService.getChallenge(savedUser, savedChallenge.getId());

        assertEquals(savedChallenge, challengeRes);
    }

    @Test
    void testGetChallengeForWhenUserIsNotChallengeOwner() {
        // This test is likely unnecessary because I'm mocking
        // challengeParticipantService
        User challengeOwner = saveRandomUser();
        Challenge challengeToSave = new Challenge();
        challengeToSave.setOwner(challengeOwner);
        challengeToSave.setName("my challenge");
        Challenge savedChallenge = challengeRepo.save(challengeToSave);

        when(challengeParticipantService.isParticipant(savedUser, savedChallenge)).thenReturn(true);

        // savedUser is not the challenge owner
        Challenge challengeRes = challengeService.getChallenge(savedUser, savedChallenge.getId());

        assertEquals(savedChallenge, challengeRes);
    }

    @Test
    void testGetChallengeNotFound() {
        Challenge challengeToSave = new Challenge();
        challengeToSave.setOwner(savedUser);
        challengeToSave.setName("my challenge");
        Challenge savedChallenge = challengeRepo.save(challengeToSave);

        // should not fail due to participant check (even though this check should not
        // be run for this condition)
        when(challengeParticipantService.isParticipant(savedUser, savedChallenge)).thenReturn(true);
        Long invalidChallengeId = 9999L;

        assertThrows(NotFoundException.class, () -> challengeService.getChallenge(savedUser, invalidChallengeId));
    }

    @Test
    void testGetChallengeUserIsNotParticipant() {
        Challenge challengeToSave = new Challenge();
        challengeToSave.setOwner(savedUser);
        challengeToSave.setName("my challenge");
        Challenge savedChallenge = challengeRepo.save(challengeToSave);

        when(challengeParticipantService.isParticipant(savedUser, savedChallenge)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> challengeService.getChallenge(savedUser, savedChallenge.getId()));

    }
}
