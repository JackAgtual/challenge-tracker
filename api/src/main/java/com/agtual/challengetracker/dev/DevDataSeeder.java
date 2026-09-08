package com.agtual.challengetracker.dev;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.GoalCompletion;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.entity.Invite;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ChallengeStatus;
import com.agtual.challengetracker.enums.InviteStatus;
import com.agtual.challengetracker.repo.ChallengeRepo;
import com.agtual.challengetracker.repo.GoalCompletionRepo;
import com.agtual.challengetracker.repo.GoalDefinitionRepo;
import com.agtual.challengetracker.repo.InviteRepo;
import com.agtual.challengetracker.repo.ParticipantRepo;
import com.agtual.challengetracker.repo.UserRepo;

/**
 * Populates the database with representative data across every state
 * (challenge lifecycle stages, participant readiness, invite outcomes,
 * and goal-completion history) for local development only.
 */
@Component
@Profile("dev")
@lombok.RequiredArgsConstructor
public class DevDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DevDataSeeder.class);

    private final UserRepo userRepository;
    private final ChallengeRepo challengeRepository;
    private final ParticipantRepo participantRepository;
    private final GoalDefinitionRepo goalDefinitionRepository;
    private final GoalCompletionRepo goalCompletionRepository;
    private final InviteRepo inviteRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Dev data already present, skipping seed.");
            return;
        }

        log.info("Seeding dev data...");

        // ---------- Users ----------
        User bob = new User();
        bob.setAuthSubject("auth0|6a97b0e82c35e6fe49eda7b8");
        bob.setEmail("dev1@gmail.com");
        bob.setFirstName("Bob");
        bob.setLastName("Smith");
        bob.setUsername("iAmDev1");
        userRepository.save(bob);

        User andy = new User();
        andy.setAuthSubject("auth0|6a97b26f2c35e6fe49eda8db");
        andy.setEmail("dev2@gmail.com");
        andy.setFirstName("Andy");
        andy.setLastName("Gump");
        andy.setUsername("agump99");
        userRepository.save(andy);

        User alice = new User();
        alice.setAuthSubject("auth0|6a94c674f3781b6583e07329");
        alice.setEmail("alice.test@gmail.com");
        alice.setFirstName("Alice");
        alice.setLastName("Doe");
        alice.setUsername("adoe3");
        userRepository.save(alice);

        // ---------- Challenges (one per lifecycle state) ----------

        // PENDING: not started yet, still configurable, no goals/completions
        Challenge plankChallenge = new Challenge();
        plankChallenge.setOwner(bob);
        plankChallenge.setName("30-Day Plank Challenge");
        plankChallenge.setDurationDays(30);
        plankChallenge.setStatus(ChallengeStatus.PENDING);
        challengeRepository.save(plankChallenge);

        // ACTIVE: in progress, partial completion history
        Challenge runStreak = new Challenge();
        runStreak.setOwner(bob);
        runStreak.setName("Morning Run Streak");
        runStreak.setStartDate(LocalDate.now().minusDays(5));
        runStreak.setDurationDays(21);
        runStreak.setStatus(ChallengeStatus.IN_PROGRESS);
        challengeRepository.save(runStreak);

        // COMPLETED: finished, full/near-full completion history
        Challenge noSugarNov = new Challenge();
        noSugarNov.setOwner(andy);
        noSugarNov.setName("No Sugar November");
        noSugarNov.setStartDate(LocalDate.now().minusDays(45));
        noSugarNov.setDurationDays(30);
        noSugarNov.setStatus(ChallengeStatus.COMPLETE);
        challengeRepository.save(noSugarNov);

        // ---------- Participants (mix of ready / not ready) ----------

        // Pending challenge: owner joined but not ready, invited user hasn't set up
        // goals
        newParticipant(bob, plankChallenge, false);
        newParticipant(andy, plankChallenge, true);

        // Active challenge: both participants ready and mid-way through
        Participant bobOnRunStreak = newParticipant(bob, runStreak, true);
        Participant aliceOnRunStreak = newParticipant(alice, runStreak, true);

        // Completed challenge: both participants were ready
        Participant andyOnNoSugar = newParticipant(andy, noSugarNov, true);
        Participant bobOnNoSugar = newParticipant(bob, noSugarNov, true);

        // ---------- Goal definitions + completions ----------

        // Pending challenge has no goal definitions yet (nothing to track before it
        // starts)

        // Active challenge: partial history (5 days in, some days missed)
        GoalDefinition bobRunGoal = newGoalDefinition(bobOnRunStreak, "Run 2 miles");
        addCompletions(bobRunGoal, runStreak.getStartDate(), 5, new int[] { 0, 1, 3, 4 }); // missed day 2

        GoalDefinition aliceRunGoal = newGoalDefinition(aliceOnRunStreak, "Run 1 mile");
        addCompletions(aliceRunGoal, runStreak.getStartDate(), 5, new int[] { 0, 2, 4 }); // more misses

        GoalDefinition aliceStretchGoal = newGoalDefinition(aliceOnRunStreak, "Stretch 10 minutes");
        addCompletions(aliceStretchGoal, runStreak.getStartDate(), 5, new int[] { 0, 1, 2, 3, 4 }); // perfect streak

        // Completed challenge: near-full history
        GoalDefinition andyDessertGoal = newGoalDefinition(andyOnNoSugar, "No dessert");
        addCompletions(andyDessertGoal, noSugarNov.getStartDate(), 30, allDaysExcept(30, 17)); // one slip-up

        GoalDefinition bobSodaGoal = newGoalDefinition(bobOnNoSugar, "No soda");
        addCompletions(bobSodaGoal, noSugarNov.getStartDate(), 30, allDaysExcept(30, 3, 12, 19, 20, 27)); // rougher
                                                                                                          // month

        // ---------- Invites (one per outcome) ----------

        Invite pendingInvite = new Invite();
        pendingInvite.setChallenge(plankChallenge);
        pendingInvite.setInviteSender(bob);
        pendingInvite.setInvitedUser(andy);
        pendingInvite.setStatus(InviteStatus.PENDING);
        inviteRepository.save(pendingInvite);

        Invite acceptedInvite = new Invite();
        acceptedInvite.setChallenge(runStreak);
        acceptedInvite.setInviteSender(bob);
        acceptedInvite.setInvitedUser(alice);
        acceptedInvite.setStatus(InviteStatus.ACCEPTED);
        inviteRepository.save(acceptedInvite);

        Invite acceptedInvite2 = new Invite();
        acceptedInvite2.setChallenge(noSugarNov);
        acceptedInvite2.setInviteSender(andy);
        acceptedInvite2.setInvitedUser(bob);
        acceptedInvite2.setStatus(InviteStatus.ACCEPTED);
        inviteRepository.save(acceptedInvite2);

        Invite declinedInvite = new Invite();
        declinedInvite.setChallenge(noSugarNov);
        declinedInvite.setInviteSender(andy);
        declinedInvite.setInvitedUser(alice);
        declinedInvite.setStatus(InviteStatus.DECLINED);
        inviteRepository.save(declinedInvite);

        log.info("Dev data seeding complete: {} users, {} challenges, {} participants, {} invites",
                userRepository.count(), challengeRepository.count(),
                participantRepository.count(), inviteRepository.count());
    }

    private Participant newParticipant(User user, Challenge challenge, boolean ready) {
        Participant participant = new Participant();
        participant.setUser(user);
        participant.setChallenge(challenge);
        participant.setReady(ready);
        return participantRepository.save(participant);
    }

    private GoalDefinition newGoalDefinition(Participant participant, String name) {
        GoalDefinition goalDefinition = new GoalDefinition();
        goalDefinition.setParticipant(participant);
        goalDefinition.setName(name);
        return goalDefinitionRepository.save(goalDefinition);
    }

    /**
     * Saves a GoalCompletion for each offset (day index from startDate) in
     * completedDayIndexes
     */
    private void addCompletions(GoalDefinition goalDefinition, LocalDate startDate, int totalDays,
            int[] completedDayIndexes) {
        for (int dayIndex : completedDayIndexes) {
            if (dayIndex > totalDays) {
                throw new RuntimeException("Completed days must be less than totalDays of challenge");
            }
            GoalCompletion completion = new GoalCompletion();
            completion.setGoalDefinition(goalDefinition);
            completion.setCompletedDate(startDate.plusDays(dayIndex));
            goalCompletionRepository.save(completion);
        }
    }

    /**
     * Returns 0..totalDays-1 excluding the given day indexes (for "missed a few
     * days" scenarios)
     */
    private int[] allDaysExcept(int totalDays, int... excluded) {
        return java.util.stream.IntStream.range(0, totalDays)
                .filter(day -> java.util.stream.IntStream.of(excluded).noneMatch(ex -> ex == day))
                .toArray();
    }
}