package com.agtual.challengetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.agtual.challengetracker.dto.request.CreateChallengeRequest;
import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.exception.AlreadyExistsException;
import com.agtual.challengetracker.repo.ChallengeRepo;
import com.agtual.challengetracker.repo.UserRepo;
import com.agtual.challengetracker.testsupport.TestUtils;

@DataJpaTest
@Import(ChallengeService.class)
public class ChallengeServiceTest {

    static Jwt jwt = TestUtils.buildJwt("subject");
    static CreateChallengeRequest createChallengeRequest = new CreateChallengeRequest("my challenge", 30);

    @Autowired
    ChallengeService challengeService;

    @Autowired
    ChallengeRepo challengeRepo;

    @Autowired
    UserRepo userRepo;

    @MockitoBean
    UserService userService;

    @MockitoBean
    ChallengeParticipantService challengeParticipantService;

    private User savedUser;

    @BeforeEach
    void beforeEach() {
        User user = new User();
        user.setEmail("bob@gmail.com");
        savedUser = userRepo.save(user);
        when(userService.getValidUser(any(Jwt.class))).thenReturn(savedUser);
    }

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
