package com.agtual.challengetracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.ChallengeParticipant;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.InviteStatus;
import com.agtual.challengetracker.enums.ResourceType;
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
        Optional<ChallengeParticipant> participant = challengeParticipantRepo.findByParticipantAndChallengeId(user,
                challengeId);
        if (participant.isEmpty()) {
            throw new NotFoundException(ResourceType.CHALLENGE_PARTICIPANT, "challengeId=" + challengeId);
        }
        return participant.get();
    }
}
