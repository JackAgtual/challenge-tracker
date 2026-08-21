package com.agtual.challengetracker.testutil;

import java.time.LocalDate;
import java.util.UUID;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.ChallengeParticipant;
import com.agtual.challengetracker.entity.CompletedGoal;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.entity.User;

public class TestEntityFactory {
    public static User validUser() {
        User user = new User();
        user.setEmail("user-" + UUID.randomUUID() + "@gmail.com");
        user.setAuthSubject("auth|" + UUID.randomUUID().toString());
        user.setFirstName("John");
        user.setLastName("Doe");
        return user;
    }

    public static Challenge validChallenge(User user, String name) {
        Challenge challenge = new Challenge();
        challenge.setOwner(user);
        challenge.setName(name);
        return challenge;
    }

    public static ChallengeParticipant validChallengeParticipant(User user, Challenge challenge) {
        ChallengeParticipant challengeParticipant = new ChallengeParticipant();
        challengeParticipant.setParticipant(user);
        challengeParticipant.setChallenge(challenge);
        return challengeParticipant;
    }

    public static GoalDefinition validGoalDefinition(ChallengeParticipant participant, String name) {
        GoalDefinition goalDefinition = new GoalDefinition();
        goalDefinition.setParticipant(participant);
        goalDefinition.setName(name);
        return goalDefinition;
    }

    public static CompletedGoal validCompletedGoal(GoalDefinition goalDefinition, LocalDate date) {
        CompletedGoal completedGoal = new CompletedGoal();
        completedGoal.setGoalDefinition(goalDefinition);
        completedGoal.setCompletedDate(date);
        return completedGoal;
    }
}
