package com.agtual.challengetracker.testutil;

import java.time.LocalDate;
import java.util.UUID;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.GoalCompletion;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.entity.Invite;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.InviteStatus;

public class TestEntityFactory {
    public static User validUser() {
        User user = new User();
        user.setEmail("user-" + UUID.randomUUID() + "@gmail.com");
        user.setAuthSubject("auth|" + UUID.randomUUID());
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername(UUID.randomUUID().toString());
        return user;
    }

    public static Challenge validChallenge(User user, String name) {
        Challenge challenge = new Challenge();
        challenge.setOwner(user);
        challenge.setName(name);
        return challenge;
    }

    public static Participant validParticipant(User user, Challenge challenge) {
        Participant participant = new Participant();
        participant.setUser(user);
        participant.setChallenge(challenge);
        return participant;
    }

    public static GoalDefinition validGoalDefinition(Participant participant, String name) {
        GoalDefinition goalDefinition = new GoalDefinition();
        goalDefinition.setParticipant(participant);
        goalDefinition.setName(name);
        return goalDefinition;
    }

    public static GoalCompletion validGoalCompletion(GoalDefinition goalDefinition, LocalDate date) {
        GoalCompletion completedGoal = new GoalCompletion();
        completedGoal.setGoalDefinition(goalDefinition);
        completedGoal.setCompletedDate(date);
        return completedGoal;
    }

    public static Invite validInvite(Challenge challenge, User inviteSender, User userToInvite) {
        Invite invite = new Invite();
        invite.setChallenge(challenge);
        invite.setInviteSender(inviteSender);
        invite.setInvitedUser(userToInvite);
        return invite;
    }

    public static Invite validInvite(Challenge challenge, User inviteSender, User userToInvite, InviteStatus status) {
        Invite invite = new Invite();
        invite.setChallenge(challenge);
        invite.setInviteSender(inviteSender);
        invite.setInvitedUser(userToInvite);
        invite.setStatus(status);
        return invite;
    }
}
