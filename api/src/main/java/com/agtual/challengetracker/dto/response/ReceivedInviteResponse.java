package com.agtual.challengetracker.dto.response;

import java.time.Clock;

import com.agtual.challengetracker.entity.Invite;

import jakarta.validation.constraints.NotNull;

public record ReceivedInviteResponse(@NotNull Long id, @NotNull boolean canRespond,
        @NotNull ChallengeResponse challenge,
        @NotNull ValidUserResponse inviteSender) {
    public static ReceivedInviteResponse from(Invite invite, Clock clock) {
        return new ReceivedInviteResponse(
                invite.getId(), invite.recipientCanRespond(), ChallengeResponse.from(invite.getChallenge(), clock),
                ValidUserResponse.from(invite.getInviteSender()));
    }
}
