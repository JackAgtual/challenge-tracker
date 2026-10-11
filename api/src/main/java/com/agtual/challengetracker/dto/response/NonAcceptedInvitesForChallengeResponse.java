package com.agtual.challengetracker.dto.response;

import com.agtual.challengetracker.entity.Invite;
import com.agtual.challengetracker.enums.InviteStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NonAcceptedInvitesForChallengeResponse(@NotNull UserResponse user, @NotBlank InviteStatus inviteStatus) {
    public static NonAcceptedInvitesForChallengeResponse from(Invite invite) {
        return new NonAcceptedInvitesForChallengeResponse(UserResponse.from(invite.getInvitedUser()),
                invite.getStatus());
    }
}
