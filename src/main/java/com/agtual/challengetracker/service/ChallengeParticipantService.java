package com.agtual.challengetracker.service;

import org.springframework.stereotype.Service;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.ChallengeParticipant;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.InviteStatus;
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
}
