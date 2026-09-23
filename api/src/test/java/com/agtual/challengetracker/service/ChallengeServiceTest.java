package com.agtual.challengetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.agtual.challengetracker.dto.request.CreateChallengeRequest;
import com.agtual.challengetracker.dto.request.ModifyChallengeRequest;
import com.agtual.challengetracker.dto.response.ChallengeReadyResponse;
import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ChallengeStatus;
import com.agtual.challengetracker.exception.AlreadyExistsException;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.ChallengeRepo;
import com.agtual.challengetracker.testutil.MockUserBaseTest;
import com.agtual.challengetracker.testutil.TestEntityFactory;

@DataJpaTest
@Import(ChallengeService.class)
public class ChallengeServiceTest extends MockUserBaseTest {

    @Autowired
    ChallengeService challengeService;

    @Autowired
    ChallengeRepo challengeRepo;

    @MockitoBean
    ParticipantService participantService;

    @Nested
    class CreateChallenge {
        static CreateChallengeRequest createChallengeRequest = new CreateChallengeRequest("my challenge", 30);

        @Test
        void testCreateChallenge() {
            Challenge challenge = challengeService.createChallenge(savedUser, createChallengeRequest);

            assertEquals(createChallengeRequest.name(), challenge.getName());
            assertEquals(createChallengeRequest.durationDays(), challenge.getDurationDays());
            assertEquals(ChallengeStatus.PENDING, challenge.getStatus());

            Challenge challengeFromRepo = challengeRepo.findById(challenge.getId()).get();
            assertEquals(challenge, challengeFromRepo);
        }

        @Test
        void testCreateChallengeThrowsExceptionIfUserAlreadyOwnsChallengeWithSameName() {
            Challenge existingChallenge = TestEntityFactory.validChallenge(savedUser, createChallengeRequest.name());

            challengeRepo.save(existingChallenge);

            assertThrows(AlreadyExistsException.class,
                    () -> challengeService.createChallenge(savedUser, createChallengeRequest));
        }

        @Test
        void testCreateChallengeAddsOwnerAsParticipant() {
            Challenge challenge = challengeService.createChallenge(savedUser, createChallengeRequest);
            verify(participantService).addUserToChallenge(savedUser, challenge);
        }
    }

    @Nested
    class NonCreateActions {
        Challenge savedChallenge;

        @BeforeEach
        void beforeEach() {
            Challenge challengeToSave = TestEntityFactory.validChallenge(savedUser, "my challenge");
            challengeToSave.setDurationDays(30);
            savedChallenge = challengeRepo.save(challengeToSave);
        }

        @Nested
        class GetChallengeOwner {

            Participant expectedOwner;

            void beforeEach() {
                expectedOwner = mock(Participant.class);
                when(participantService.getChallengeParticipationForUserAndChallengeId(savedChallenge.getOwner(),
                        savedChallenge.getId())).thenReturn(expectedOwner);
            }

            @Test
            void testGetChallengeOwner() {
                when(participantService.isParticipant(savedUser, savedChallenge.getId())).thenReturn(true);

                Participant owner = challengeService.getChallengeOwner(savedUser, savedChallenge.getId());
                assertEquals(expectedOwner, owner);
            }

            @Test
            void testGetChallengeOwnerChallengeDoesntExist() {
                when(participantService.isParticipant(savedUser, savedChallenge.getId())).thenReturn(true);

                assertThrows(NotFoundException.class, () -> challengeService.getChallengeOwner(savedUser, 9999L));
            }

            @Test
            void testGetChallengeOwnerRequestorIsNotParticipant() {
                when(participantService.isParticipant(savedUser, savedChallenge.getId())).thenReturn(false);
                assertThrows(NotFoundException.class,
                        () -> challengeService.getChallengeOwner(savedUser, savedChallenge.getId()));

            }
        }

        @Nested
        class GetChallenge {

            @Test
            void testGetChallenge() {
                when(participantService.isParticipant(savedUser, savedChallenge)).thenReturn(true);

                Challenge challengeRes = challengeService.getChallenge(savedUser, savedChallenge.getId());

                assertEquals(savedChallenge, challengeRes);
            }

            @Test
            void testGetChallengeForWhenUserIsNotChallengeOwner() {
                // This test is likely unnecessary because I'm mocking participantService
                User challengeOwner = saveRandomUser();
                Challenge challengeToSave = TestEntityFactory.validChallenge(challengeOwner, "my_challenge");
                Challenge savedChallenge = challengeRepo.save(challengeToSave);

                when(participantService.isParticipant(savedUser, savedChallenge)).thenReturn(true);

                // savedUser is not the challenge owner
                Challenge challengeRes = challengeService.getChallenge(savedUser, savedChallenge.getId());

                assertEquals(savedChallenge, challengeRes);
            }

            @Test
            void testGetChallengeNotFound() {
                // should not fail due to participant check (even though this check should not
                // be run for this condition)
                when(participantService.isParticipant(savedUser, savedChallenge)).thenReturn(true);
                Long invalidChallengeId = 9999L;

                assertThrows(NotFoundException.class,
                        () -> challengeService.getChallenge(savedUser, invalidChallengeId));
            }

            @Test
            void testGetChallengeUserIsNotParticipant() {
                when(participantService.isParticipant(savedUser, savedChallenge)).thenReturn(false);

                assertThrows(NotFoundException.class,
                        () -> challengeService.getChallenge(savedUser, savedChallenge.getId()));
            }

            @Test
            void testGetChallengeFromOwner() {
                Challenge challenge = challengeService.getChallengeFromOwner(savedChallenge.getId(), savedUser);
                assertEquals(savedChallenge, challenge);
            }

            @Test
            void testGetChallengeFromOwnerWrongOwner() {
                User nonOwner = saveRandomUser();
                assertThrows(NotFoundException.class,
                        () -> challengeService.getChallengeFromOwner(savedChallenge.getId(), nonOwner));
            }

            @Test
            void testGetChallenteFromOwnerInvalidChallenge() {
                assertThrows(NotFoundException.class,
                        () -> challengeService.getChallengeFromOwner(99999L, savedUser));

            }

        }

        @Nested
        class ModifyChallenge {

            @Test
            void testModifyChallenge() {
                ModifyChallengeRequest mod1 = new ModifyChallengeRequest("challenge mod", null);
                Challenge modifiedChallenge1 = challengeService.modifyChallenge(savedUser, savedChallenge.getId(),
                        mod1);
                assertEquals(savedChallenge.getId(), modifiedChallenge1.getId());
                assertEquals(savedChallenge.getOwner(), modifiedChallenge1.getOwner());
                assertEquals(savedChallenge.getStartDate(), modifiedChallenge1.getStartDate());
                assertEquals("challenge mod", modifiedChallenge1.getName());
                assertNull(modifiedChallenge1.getDurationDays());

                ModifyChallengeRequest mod2 = new ModifyChallengeRequest("name change", 30);
                Challenge modifiedChallenge2 = challengeService.modifyChallenge(savedUser, savedChallenge.getId(),
                        mod2);
                assertEquals(savedChallenge.getId(), modifiedChallenge2.getId());
                assertEquals(savedChallenge.getOwner(), modifiedChallenge2.getOwner());
                assertEquals(savedChallenge.getStartDate(), modifiedChallenge2.getStartDate());
                assertEquals("name change", modifiedChallenge1.getName());
                assertEquals(30, modifiedChallenge2.getDurationDays());

                ModifyChallengeRequest mod3 = new ModifyChallengeRequest("final name", 45);
                Challenge modifiedChallenge3 = challengeService.modifyChallenge(savedUser, savedChallenge.getId(),
                        mod3);
                assertEquals(savedChallenge.getId(), modifiedChallenge3.getId());
                assertEquals(savedChallenge.getOwner(), modifiedChallenge3.getOwner());
                assertEquals(savedChallenge.getStartDate(), modifiedChallenge3.getStartDate());
                assertEquals("final name", modifiedChallenge1.getName());
                assertEquals(45, modifiedChallenge3.getDurationDays());
            }

            @Test
            void testModifyChallengeNotAllowedForNonOwner() {
                User nonOwner = saveRandomUser();

                ModifyChallengeRequest mod = new ModifyChallengeRequest("name", 30);
                assertThrows(NotFoundException.class, () -> challengeService.modifyChallenge(nonOwner,
                        savedChallenge.getId(), mod));
            }

            @Test
            void testModifyChallengeNotAllowedForInProgressOrCompleteChallenge() {
                ModifyChallengeRequest mod = new ModifyChallengeRequest("name", 30);

                savedChallenge.setStatus(ChallengeStatus.IN_PROGRESS);
                challengeRepo.save(savedChallenge);
                assertThrows(ForbiddenException.class,
                        () -> challengeService.modifyChallenge(savedUser, savedChallenge.getId(), mod));

                savedChallenge.setStatus(ChallengeStatus.COMPLETE);
                challengeRepo.save(savedChallenge);
                assertThrows(ForbiddenException.class,
                        () -> challengeService.modifyChallenge(savedUser, savedChallenge.getId(), mod));
            }
        }

        /**
         * CanStartChallenge
         * 
         * Mocking challenge repo and challenge entity so I can mock return value of
         * {@link Challenge#isReadyToStart()}
         */
        @Nested
        class CanStartChallenge {

            static Long mockChallengeId = 33L;

            @MockitoBean
            ChallengeRepo challengeRepoMock;

            Challenge challengeMock;

            @BeforeEach
            void beforeEach() {
                challengeMock = mock(Challenge.class);
                when(participantService.allJoinedParticipantsAreReady(challengeMock)).thenReturn(true);
                when(challengeMock.isReadyToStart()).thenReturn(true);
                when(challengeRepoMock.findByOwnerAndId(savedUser, mockChallengeId))
                        .thenReturn(Optional.of(challengeMock));
            }

            @Test
            void testCanStartChallenge() {
                assertTrue(challengeService.canStartChallenge(savedUser, mockChallengeId).ready());
            }

            @Test
            void testCanStartChallengeInvalidChallengeId() {
                when(challengeRepoMock.findByOwnerAndId(savedUser, mockChallengeId))
                        .thenReturn(Optional.empty());
                assertThrows(NotFoundException.class,
                        () -> challengeService.canStartChallenge(savedUser, mockChallengeId));
            }

            @Test
            void testCanStartChallengeChallengeNotReady() {
                when(challengeMock.isReadyToStart()).thenReturn(false);
                assertFalse(challengeService.canStartChallenge(savedUser, mockChallengeId).ready());
            }

            @Test
            void testCanStartChallengeParticipantsNotReady() {
                when(participantService.allJoinedParticipantsAreReady(challengeMock)).thenReturn(false);
                assertFalse(challengeService.canStartChallenge(savedUser, mockChallengeId).ready());
            }

            @Test
            void testCanStartChallengeReasons() {
                when(challengeMock.isReadyToStart()).thenReturn(false);
                when(participantService.allJoinedParticipantsAreReady(challengeMock)).thenReturn(false);

                List<String> reasons = challengeService.canStartChallenge(savedUser, mockChallengeId).reasons();

                assertEquals(2, reasons.size());
                assertTrue(reasons.containsAll(List.of("Challenge must have name, duration and be pending",
                        "Not all participants are ready")));
            }

            @Test
            void testCanStartChallengeOnlyChallengeNotReady() {
                when(challengeMock.isReadyToStart()).thenReturn(false);

                List<String> reasons = challengeService.canStartChallenge(savedUser, mockChallengeId).reasons();

                assertEquals(1, reasons.size());
                assertTrue(reasons.contains("Challenge must have name, duration and be pending"));

            }

            @Test
            void testCanStartChallengeOnlyParticipantsNotReady() {
                when(participantService.allJoinedParticipantsAreReady(challengeMock)).thenReturn(false);

                List<String> reasons = challengeService.canStartChallenge(savedUser, mockChallengeId).reasons();

                assertEquals(1, reasons.size());
                assertTrue(reasons.contains("Not all participants are ready"));

            }
        }

        @Nested
        class StartChallenge {

            @MockitoSpyBean
            ChallengeService challengeService;

            @BeforeEach
            void beforeEach() {
                ChallengeReadyResponse readyChallengeRes = new ChallengeReadyResponse(true, Collections.emptyList());
                when(participantService.allJoinedParticipantsAreReady(savedChallenge)).thenReturn(true);
                when(challengeService.canStartChallenge(savedUser, savedChallenge.getId()))
                        .thenReturn(readyChallengeRes);
            }

            @Test
            void testStartChallenge() {
                Challenge startedChallenge = challengeService.startChallenge(savedUser, savedChallenge.getId());

                assertEquals(ChallengeStatus.IN_PROGRESS, startedChallenge.getStatus());
                assertEquals(LocalDate.now(), startedChallenge.getStartDate());
                assertEquals(startedChallenge, challengeRepo.findById(startedChallenge.getId()).get());
            }

            @Test
            void testStartChallengeFromNonOwner() {
                User nonOwner = saveRandomUser();
                assertThrows(NotFoundException.class,
                        () -> challengeService.startChallenge(nonOwner, savedChallenge.getId()));
            }

            @Test
            void testStartChallengeChallengeNotReady() {
                ChallengeReadyResponse notReadyChallengeRes = new ChallengeReadyResponse(false,
                        List.of("Not all participants are ready"));
                when(challengeService.canStartChallenge(savedUser, savedChallenge.getId())).thenReturn(
                        notReadyChallengeRes);
                assertThrows(ForbiddenException.class,
                        () -> challengeService.startChallenge(savedUser, savedChallenge.getId()));
            }
        }
    }

}
