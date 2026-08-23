package com.agtual.challengetracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.ChallengeParticipant;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ChallengeStatus;
import com.agtual.challengetracker.enums.InviteStatus;
import com.agtual.challengetracker.enums.ResourceType;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.ChallengeParticipantRepo;

@Service
@lombok.RequiredArgsConstructor
public class ChallengeParticipantService {

    private final ChallengeParticipantRepo challengeParticipantRepo;

    public ChallengeParticipant addOwnerToChallenge(User user, Challenge challenge) {
        ChallengeParticipant challengeOwner = new ChallengeParticipant();
        challengeOwner.setParticipant(user);
        challengeOwner.setChallenge(challenge);
        challengeOwner.setInviteStatus(InviteStatus.ACCEPTED);
        return challengeParticipantRepo.save(challengeOwner);
    }

    public boolean allJoinedParticipantsAreReady(Challenge challenge) {
        List<ChallengeParticipant> participants = challengeParticipantRepo.findByChallenge(challenge);
        if (participants.isEmpty()) {
            return false;
        }

        return participants.stream()
                .filter(participant -> participant.getInviteStatus() == InviteStatus.ACCEPTED)
                .allMatch(participant -> participant.isReady());
    }

    public boolean isParticipant(User user, Challenge challenge) {
        return challengeParticipantRepo.existsByChallengeAndParticipant(challenge, user);
    }

    public List<ChallengeParticipant> getAllChallengeParticipationsForUser(User user) {
        return challengeParticipantRepo.findByParticipant(user).stream()
                .filter(p -> p.getInviteStatus() != InviteStatus.DECLINED)
                .toList();
    }

    public ChallengeParticipant getChallengeParticipationForUserAndChallengeId(User user, Long challengeId) {
        return challengeParticipantRepo.findByParticipantAndChallengeId(user,
                challengeId).orElseThrow(
                        () -> new NotFoundException(ResourceType.CHALLENGE_PARTICIPANT, "challengeId=" + challengeId));
    }

    public ChallengeParticipant setReady(User user, Long challengeId, boolean ready) {
        ChallengeParticipant participant = getChallengeParticipationForUserAndChallengeId(user, challengeId);
        participant.setReady(ready);

        if (participant.getChallenge().getStatus() != ChallengeStatus.PENDING) {
            throw new ForbiddenException("Can only change ready state when challenge is pending");
        }

        return challengeParticipantRepo.save(participant);
    }

    public void ownerRemovesParticipantFromChallenge(User challengeOwner, Long challengeParticipantId) {
        ChallengeParticipant participant = challengeParticipantRepo
                .findById(challengeParticipantId)
                .orElseThrow(() -> new NotFoundException(ResourceType.CHALLENGE_PARTICIPANT, challengeParticipantId));

        Challenge challenge = participant.getChallenge();
        if (!challenge.getOwner().getId().equals(challengeOwner.getId())) {
            throw new NotFoundException(ResourceType.CHALLENGE, "participant id", challengeParticipantId);
        }

        removeParticipantFromChallenge(participant, challenge);
    }

    public void leaveChallenge(User user, Long challengeId) {
        ChallengeParticipant participant = challengeParticipantRepo.findByParticipantAndChallengeId(user, challengeId)
                .orElseThrow(() -> new NotFoundException(ResourceType.CHALLENGE_PARTICIPANT, "?"));

        Challenge challenge = participant.getChallenge();
        if (challenge.getOwner().getId().equals(user.getId())) {
            throw new ForbiddenException("Challenge owner can't leave challenge");
        }

        removeParticipantFromChallenge(participant, challenge);
    }

    private void removeParticipantFromChallenge(ChallengeParticipant participant, Challenge challenge) {
        // challenge must be pending
        if (challenge.getStatus() != ChallengeStatus.PENDING) {
            throw new ForbiddenException("Can only remove challenge participant when challenge status is pending");
        }

        challengeParticipantRepo.delete(participant);
    }

}
