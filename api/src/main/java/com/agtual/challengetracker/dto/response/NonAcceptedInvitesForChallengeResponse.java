package com.agtual.challengetracker.dto.response;

import com.agtual.challengetracker.entity.Invite;
import com.agtual.challengetracker.enums.InviteStatus;

import jakarta.validation.constraints.NotBlank;

public record NonAcceptedInvitesForChallengeResponse(@NotBlank String username, @NotBlank InviteStatus inviteStatus) {
    public static NonAcceptedInvitesForChallengeResponse from(Invite invite) {
        return new NonAcceptedInvitesForChallengeResponse(invite.getInvitedUser().getUsername(), invite.getStatus());
    }
}
