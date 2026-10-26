package com.agtual.challengetracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.Invite;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ChallengeStatus;
import com.agtual.challengetracker.enums.InviteStatus;
import com.agtual.challengetracker.enums.ResourceType;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.InviteRepo;

@Service
@lombok.RequiredArgsConstructor
public class InviteService {

    private final ChallengeService challengeService;
    private final ParticipantService participantService;
    private final InviteRepo inviteRepo;

    public List<Invite> getPendingInvites(User invitedUser) {
        return inviteRepo.findByInvitedUserAndStatus(invitedUser, InviteStatus.PENDING);
    }

    public List<Invite> getNonAcceptedInvitesForChallenge(User user, Long challengeId) {
        // User will only have access if they are paticipant (cannot just be invited)
        if (!participantService.isParticipant(user, challengeId)) {
            throw new NotFoundException(ResourceType.CHALLENGE, challengeId);
        }

        return inviteRepo.findByChallengeIdAndStatusNotOrderByInvitedAtAsc(challengeId, InviteStatus.ACCEPTED);
    }

    public Invite inviteToChallenge(User challengeOwner, Long challengeId, User userToInvite) {
        Challenge challenge = challengeService.getChallengeFromOwner(challengeId, challengeOwner);

        if (participantService.isParticipant(userToInvite, challengeId)) {
            throw new ForbiddenException("User is already a participant in challenge");
        }

        Optional<Invite> existingInvite = inviteRepo.findByChallengeAndInvitedUser(challenge,
                userToInvite);
        if (existingInvite.isPresent()) {
            throw new ForbiddenException(ResourceType.INVITE, existingInvite.get().getId(),
                    "User has already been invited to challenge");
        }

        Invite invite = new Invite();
        invite.setChallenge(challenge);
        invite.setInviteSender(challengeOwner);
        invite.setInvitedUser(userToInvite);
        invite.setStatus(InviteStatus.PENDING);

        return inviteRepo.save(invite);

        // TODO: Send email
    }

    @Transactional
    public void acceptInvite(User invitedUser, Long inviteId) {
        Invite invite = getValidInviteForUserAndConfigurableChallenge(inviteId, invitedUser);
        invite.setStatus(InviteStatus.ACCEPTED);

        participantService.addUserToChallenge(invitedUser, invite.getChallenge());
        inviteRepo.save(invite);
    }

    public void declineInvite(User invitedUser, Long inviteId) {
        Invite invite = getValidInviteForUserAndConfigurableChallenge(inviteId, invitedUser);

        invite.setStatus(InviteStatus.DECLINED);
        inviteRepo.save(invite);
    }

    public void deleteInvite(User invitedUser, Long inviteId) {
        Invite invite = getValidInviteForUser(inviteId, invitedUser);

        if (invite.getStatus() != InviteStatus.PENDING) {
            throw new ForbiddenException(ResourceType.INVITE, invite.getId(), "Can only delete pending invite");
        }

        if (invite.getChallenge().getStatus() == ChallengeStatus.PENDING) {
            throw new ForbiddenException(ResourceType.INVITE, invite.getId(),
                    "Can only delete invite for in progress or complete challenges");
        }

        inviteRepo.delete(invite);
    }

    private Invite getValidInviteForUserAndConfigurableChallenge(Long inviteId, User invitedUser) {
        Invite invite = getValidInviteForUser(inviteId, invitedUser);

        if (!invite.getChallenge().isConfigurable()) {
            throw new ForbiddenException(ResourceType.INVITE, inviteId,
                    "Can't respond to invite because challenge has already started or completed");
        }

        return invite;
    }

    private Invite getValidInviteForUser(Long inviteId, User invitedUser) {
        return inviteRepo.findByIdAndInvitedUser(inviteId, invitedUser)
                .orElseThrow(() -> new NotFoundException(ResourceType.INVITE, inviteId));
    }
}
