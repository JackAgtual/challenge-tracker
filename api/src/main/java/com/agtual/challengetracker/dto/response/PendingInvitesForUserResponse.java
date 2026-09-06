package com.agtual.challengetracker.dto.response;

import com.agtual.challengetracker.entity.Invite;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PendingInvitesForUserResponse(@NotNull Long id, @NotNull ChallengeNameResponse challengeName,
        @NotEmpty String inviteSenderUsername) {
    public static PendingInvitesForUserResponse from(Invite invite) {
        ChallengeNameResponse challengeName = new ChallengeNameResponse(
                invite.getChallenge().getId(),
                invite.getChallenge().getName());
        return new PendingInvitesForUserResponse(
                invite.getId(), challengeName, invite.getInviteSender().getUsername());
    }
}
