package com.agtual.challengetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.agtual.challengetracker.dto.request.CreateChallengeRequest;
import com.agtual.challengetracker.dto.request.ModifyChallengeRequest;
import com.agtual.challengetracker.entity.Challenge;
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
            verify(participantService).addOwnerToChallenge(savedUser, challenge);
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

        @Nested
        class StartChallenge {

            @BeforeEach
            void beforeEach() {
                when(participantService.allJoinedParticipantsAreReady(savedChallenge)).thenReturn(true);
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
            void testStartParticipantsNotReady() {
                when(participantService.allJoinedParticipantsAreReady(savedChallenge)).thenReturn(false);
                assertThrows(ForbiddenException.class,
                        () -> challengeService.startChallenge(savedUser, savedChallenge.getId()));
            }

            @Test
            void testStartChallengeChallengeNotReady() {
                savedChallenge.setDurationDays(null);
                challengeRepo.save(savedChallenge);
                assertThrows(ForbiddenException.class,
                        () -> challengeService.startChallenge(savedUser, savedChallenge.getId()));
            }

            @Test
            void testCantStartChallengeThatIsntPending() {
                savedChallenge.setStatus(ChallengeStatus.IN_PROGRESS);
                challengeRepo.save(savedChallenge);
                assertThrows(ForbiddenException.class,
                        () -> challengeService.startChallenge(savedUser, savedChallenge.getId()));

                savedChallenge.setStatus(ChallengeStatus.COMPLETE);
                challengeRepo.save(savedChallenge);
                assertThrows(ForbiddenException.class,
                        () -> challengeService.startChallenge(savedUser, savedChallenge.getId()));
            }
        }
    }

}
