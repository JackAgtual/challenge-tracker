package com.agtual.challengetracker.dto.response;

import com.agtual.challengetracker.entity.Invite;

public record PendingInvitesForUserResponse(Long id, ChallengeNameResponse challengeName, String inviteSenderUsername) {
    public static PendingInvitesForUserResponse from(Invite invite) {
        ChallengeNameResponse challengeName = new ChallengeNameResponse(
                invite.getChallenge().getId(),
                invite.getChallenge().getName());
        return new PendingInvitesForUserResponse(
                invite.getId(), challengeName, invite.getInviteSender().getFirstName());
    }
}
