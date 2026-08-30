package com.agtual.challengetracker.dto.response;

import com.agtual.challengetracker.entity.Invite;
import com.agtual.challengetracker.enums.InviteStatus;

public record NonAcceptedInvitesForChallengeResponse(String username, InviteStatus inviteStatus) {
    public static NonAcceptedInvitesForChallengeResponse from(Invite invite) {
        return new NonAcceptedInvitesForChallengeResponse(invite.getInvitedUser().getUsername(), invite.getStatus());
    }
}
