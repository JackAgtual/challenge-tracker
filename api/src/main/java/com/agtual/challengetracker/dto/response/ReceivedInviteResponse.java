package com.agtual.challengetracker.dto.response;

import com.agtual.challengetracker.entity.Invite;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ReceivedInviteResponse(@NotNull Long id, @NotNull ChallengeResponse challenge,
        @NotEmpty UserNameResponse inviteSender) {
    public static ReceivedInviteResponse from(Invite invite) {
        return new ReceivedInviteResponse(
                invite.getId(), ChallengeResponse.from(invite.getChallenge()),
                UserNameResponse.from(invite.getInviteSender()));
    }
}
