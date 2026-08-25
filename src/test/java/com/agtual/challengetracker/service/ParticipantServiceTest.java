package com.agtual.challengetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ChallengeStatus;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.ChallengeRepo;
import com.agtual.challengetracker.repo.GoalDefinitionRepo;
import com.agtual.challengetracker.repo.ParticipantRepo;
import com.agtual.challengetracker.testutil.MockUserBaseTest;
import com.agtual.challengetracker.testutil.TestEntityFactory;

@DataJpaTest
@Import(ParticipantService.class)
public class ParticipantServiceTest extends MockUserBaseTest {

    private Challenge savedChallenge1;

    @Autowired
    ParticipantService participantService;

    @Autowired
    ParticipantRepo participantRepo;

    @Autowired
    ChallengeRepo challengeRepo;

    @Autowired
    GoalDefinitionRepo goalDefinitionRepo;

    @Autowired
    TestEntityManager em;

    @BeforeEach
    void beforeEach() {
        savedChallenge1 = saveChallengeWithOwner(savedUser);
    }

    @Test
    void testAddUserToChallenge() {
        Participant owner = participantService.addUserToChallenge(savedUser, savedChallenge1);

        assertEquals(savedChallenge1, owner.getChallenge());
        assertEquals(savedUser, owner.getUser());
        assertEquals(false, owner.isReady());

        Participant participantInRepo = participantRepo.findById(owner.getId()).get();
        assertEquals(owner, participantInRepo);
    }

    @Test
    void testAddUserToChallengeParticipantAlreadyExists() {
        Participant validParticipant = TestEntityFactory.validParticipant(savedUser, savedChallenge1);
        Participant savedParticipant = participantRepo.save(validParticipant);

        assertThrows(ForbiddenException.class, () -> participantService.addUserToChallenge(savedUser, savedChallenge1));

        assertEquals(1, participantRepo.count());
        assertEquals(savedParticipant, participantRepo.findAll().get(0));
    }

    @Test
    void testAllJoinedParticipantsAreReadyWhenAllAreReady() {
        saveParticipant(true);
        saveParticipant(true);
        saveParticipant(true);

        assertTrue(participantService.allJoinedParticipantsAreReady(savedChallenge1));
    }

    @Test
    void testAllJoinedParticipantsAreReadyWhenNotAllAreReady() {
        saveParticipant(true);
        saveParticipant(false);
        saveParticipant(true);
        saveParticipant(false);
        saveParticipant(false);

        assertFalse(participantService.allJoinedParticipantsAreReady(savedChallenge1));
    }

    @Test
    void testIsParticipant() {
        User user1NotInChallenge = saveRandomUser();
        User user2InChallenge = saveRandomUser();
        addUserToChallenge(user2InChallenge, savedChallenge1);

        assertFalse(participantService.isParticipant(user1NotInChallenge, savedChallenge1));
        assertTrue(participantService.isParticipant(user2InChallenge, savedChallenge1));
    }

    @Test
    void testGetAllChallengeParticipationsForUser() {
        User otherUser1 = saveRandomUser();
        User otherUser2 = saveRandomUser();

        // challenge owner should not affect if user is participant
        // test with multiple challenge owneres
        Challenge challenge1 = saveChallengeWithOwner(otherUser1);
        Challenge challenge2 = saveChallengeWithOwner(otherUser1);
        Challenge challenge3 = saveChallengeWithOwner(savedUser);
        Challenge challenge4 = saveChallengeWithOwner(savedUser);
        Challenge challenge5 = saveChallengeWithOwner(otherUser2);

        Participant participant1 = TestEntityFactory.validParticipant(savedUser, challenge1);
        participant1 = participantRepo.save(participant1);

        Participant participant2 = TestEntityFactory.validParticipant(savedUser, challenge2);
        participant2 = participantRepo.save(participant2);

        Participant participant3 = TestEntityFactory.validParticipant(savedUser, challenge3);
        participant3 = participantRepo.save(participant3);

        Participant participant4 = TestEntityFactory.validParticipant(savedUser, challenge4);
        participant4 = participantRepo.save(participant4);

        // participant is a different user, should not be included in response
        Participant participant5 = TestEntityFactory.validParticipant(otherUser1, challenge5);
        participant5 = participantRepo.save(participant5);

        List<Participant> participations = participantService
                .getAllChallengeParticipationsForUser(savedUser);
        assertEquals(5, participantRepo.count());
        assertEquals(4, participations.size());
        assertTrue(participations.containsAll(List.of(participant1, participant2, participant3)));
    }

    @Test
    void testGetFromChallengeIdAndParticipant() {
        Participant participant = saveParticipant(savedUser);

        Participant participantRes = participantService
                .getChallengeParticipationForUserAndChallengeId(savedUser, savedChallenge1.getId());
        assertEquals(participant, participantRes);
    }

    @Test
    void testGetFromChallengeIdAndParticipantNotFound() {
        saveParticipant(savedUser);

        assertThrows(NotFoundException.class,
                () -> participantService.getChallengeParticipationForUserAndChallengeId(savedUser, 99999L));

        User nonParticipant = saveRandomUser();
        assertThrows(NotFoundException.class,
                () -> participantService.getChallengeParticipationForUserAndChallengeId(nonParticipant,
                        savedChallenge1.getId()));
    }

    @Test
    void testSetReady() {
        saveParticipant(savedUser);

        Participant resReady = participantService.setReady(savedUser, savedChallenge1.getId(), true);
        assertEquals(1, participantRepo.count());
        assertEquals(true, resReady.isReady());
        assertEquals(resReady, participantRepo.findById(resReady.getId()).get());

        // toggle response to not ready
        Participant resNotReady = participantService.setReady(savedUser, savedChallenge1.getId(),
                false);
        assertEquals(1, participantRepo.count());
        assertEquals(false, resNotReady.isReady());
        assertEquals(resNotReady, participantRepo.findById(resNotReady.getId()).get());
    }

    @Test
    void testCantSetReadyWhenChallengeIsInProgress() {
        Challenge inProgressChallenge = saveChallengeWithOwner(savedUser, ChallengeStatus.IN_PROGRESS);
        saveParticipant(savedUser, inProgressChallenge);

        assertThrows(ForbiddenException.class,
                () -> participantService.setReady(savedUser, inProgressChallenge.getId(), false));
        assertThrows(ForbiddenException.class,
                () -> participantService.setReady(savedUser, inProgressChallenge.getId(), true));
    }

    @Test
    void testCantSetReadyWhenChallengeIsComplete() {
        Challenge completedChallenge = saveChallengeWithOwner(savedUser, ChallengeStatus.COMPLETE);
        saveParticipant(savedUser, completedChallenge);

        assertThrows(ForbiddenException.class,
                () -> participantService.setReady(savedUser, completedChallenge.getId(), false));
        assertThrows(ForbiddenException.class,
                () -> participantService.setReady(savedUser, completedChallenge.getId(), true));
    }

    @Nested
    class RemovingParticipant {

        Challenge challenge;
        Participant challengeOwner;
        User nonChallengeOwner;
        Participant participantToRemove;
        GoalDefinition drinkWaterRemove;
        GoalDefinition runRemove;
        GoalDefinition bikeOwner;
        GoalDefinition proteinOwner;

        @BeforeEach
        void beforeEach() {
            challenge = saveChallengeWithOwner(savedUser);

            nonChallengeOwner = saveRandomUser();

            challengeOwner = participantRepo
                    .saveAndFlush(TestEntityFactory.validParticipant(savedUser, challenge));
            participantToRemove = participantRepo
                    .saveAndFlush(TestEntityFactory.validParticipant(nonChallengeOwner, challenge));

            assertEquals(2, participantRepo.count());
            assertTrue(participantRepo.findById(challengeOwner.getId()).isPresent());
            assertTrue(participantRepo.findById(participantToRemove.getId()).isPresent());

            // create goal definitions for participant and owner
            // assume goal completions don't exist because challenge hasn't started yet
            drinkWaterRemove = goalDefinitionRepo
                    .saveAndFlush(TestEntityFactory.validGoalDefinition(participantToRemove, "drink water"));
            runRemove = goalDefinitionRepo
                    .saveAndFlush(TestEntityFactory.validGoalDefinition(participantToRemove, "run 1 mile"));
            bikeOwner = goalDefinitionRepo
                    .saveAndFlush(TestEntityFactory.validGoalDefinition(challengeOwner, "bike"));
            proteinOwner = goalDefinitionRepo
                    .saveAndFlush(TestEntityFactory.validGoalDefinition(challengeOwner, "eat protein"));

            // test-ism needed to clear hibernate cache
            em.flush();
            em.clear();
        }

        @Test
        void testRemoveParticipantFromChallenge() {
            participantService.ownerRemovesParticipantFromChallenge(savedUser, participantToRemove.getId());
            assertParticipantHasBeenRemoved();
        }

        @Test
        void testCantRemoveParticipantIfChallengeInProgress() {
            Challenge challenge = saveChallengeWithOwner(savedUser, ChallengeStatus.IN_PROGRESS);
            assertRemovingParticipantFromChallengeThrowsError(challenge);
        }

        @Test
        void testCantRemoveParticipantIfChallengeComplete() {
            Challenge challenge = saveChallengeWithOwner(savedUser, ChallengeStatus.COMPLETE);

            assertRemovingParticipantFromChallengeThrowsError(challenge);
        }

        @Test
        void testLeaveChallenge() {
            participantService.leaveChallenge(nonChallengeOwner, challenge.getId());
            assertParticipantHasBeenRemoved();
        }

        @Test
        void testOwnerCantLeaveChallenge() {
            assertThrows(ForbiddenException.class,
                    () -> participantService.leaveChallenge(savedUser, challenge.getId()));
        }

        @Test
        void testCantLeaveChallengeWhenInProgress() {
            Challenge challenge = saveChallengeWithOwner(savedUser, ChallengeStatus.IN_PROGRESS);
            assertLeavingChallengeThrowsError(challenge);
        }

        @Test
        void testCantLeaveChallengeWhenComplete() {
            Challenge challenge = saveChallengeWithOwner(savedUser, ChallengeStatus.COMPLETE);
            assertLeavingChallengeThrowsError(challenge);

        }

        private void assertRemovingParticipantFromChallengeThrowsError(Challenge challenge) {
            User nonChallengeOwner = saveRandomUser();
            participantRepo
                    .saveAndFlush(TestEntityFactory.validParticipant(savedUser, challenge));
            Participant participantToRemove = participantRepo
                    .saveAndFlush(TestEntityFactory.validParticipant(nonChallengeOwner, challenge));

            assertThrows(ForbiddenException.class, () -> participantService
                    .ownerRemovesParticipantFromChallenge(savedUser, participantToRemove.getId()));
        }

        private void assertLeavingChallengeThrowsError(Challenge challenge) {
            User nonChallengeOwner = saveRandomUser();
            participantRepo.saveAndFlush(TestEntityFactory.validParticipant(savedUser, challenge));
            participantRepo
                    .saveAndFlush(TestEntityFactory.validParticipant(nonChallengeOwner, challenge));

            assertThrows(ForbiddenException.class, () -> participantService
                    .leaveChallenge(nonChallengeOwner, challenge.getId()));
        }

        private void assertParticipantHasBeenRemoved() {
            // assert challenge participant repo is changed
            assertEquals(1, participantRepo.count());
            assertTrue(participantRepo.findById(participantToRemove.getId()).isEmpty());
            assertTrue(participantRepo.findById(challengeOwner.getId()).isPresent());

            // assert goal definitions and goal completions are empty
            assertEquals(2, goalDefinitionRepo.count());
            assertTrue(goalDefinitionRepo.findById(bikeOwner.getId()).isPresent());
            assertTrue(goalDefinitionRepo.findById(proteinOwner.getId()).isPresent());
            assertTrue(goalDefinitionRepo.findById(drinkWaterRemove.getId()).isEmpty());
            assertTrue(goalDefinitionRepo.findById(runRemove.getId()).isEmpty());
        }
    }

    private void saveParticipant(boolean ready) {
        User userEntity = saveRandomUser();

        Participant participant = TestEntityFactory.validParticipant(userEntity, savedChallenge1);
        participant.setReady(ready);
        participantRepo.save(participant);
    }

    private Participant saveParticipant(User user) {
        return saveParticipant(user, savedChallenge1);
    }

    private Participant saveParticipant(User user, Challenge challenge) {
        Participant participant = TestEntityFactory.validParticipant(savedUser, challenge);
        return participantRepo.save(participant);
    }

    private void addUserToChallenge(User user, Challenge challenge) {
        Participant participant = TestEntityFactory.validParticipant(user, challenge);
        participantRepo.save(participant);
    }

    private Challenge saveChallengeWithOwner(User owner) {
        return saveChallengeWithOwner(owner, ChallengeStatus.PENDING);
    }

    private Challenge saveChallengeWithOwner(User owner, ChallengeStatus challengeStatus) {
        Challenge challenge = TestEntityFactory.validChallenge(owner, UUID.randomUUID().toString());
        challenge.setStatus(challengeStatus);
        return challengeRepo.save(challenge);
    }

}
