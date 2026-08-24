package com.agtual.challengetracker.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.Invite;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.InviteStatus;
import com.agtual.challengetracker.enums.ResourceType;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.repo.InviteRepo;

@Service
@lombok.RequiredArgsConstructor
public class InviteService {

    private final ChallengeService challengeService;
    private final InviteRepo inviteRepo;

    public Invite inviteToChallenge(User challengeOwner, Long challengeId, User userToInvite) {
        Challenge challenge = challengeService.getChallengeFromOwner(challengeId, challengeOwner);

        Optional<Invite> existingInvite = inviteRepo.findByChallengeAndInvitedUserEmail(challenge,
                userToInvite.getEmail());
        if (existingInvite.isPresent()) {
            throw new ForbiddenException(ResourceType.INVITE, existingInvite.get().getId(),
                    "User has already been invited to challenge");
        }

        Invite invite = new Invite();
        invite.setChallenge(challenge);
        invite.setInviteSender(challengeOwner);
        invite.setInvitedUser(userToInvite);
        invite.setInvitedUserEmail(userToInvite.getEmail());
        invite.setStatus(InviteStatus.PENDING);
        invite.setToken(UUID.randomUUID());

        return inviteRepo.save(invite);

        // TODO: Send email
    }
}
