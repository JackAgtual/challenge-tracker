package com.agtual.challengetracker.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agtual.challengetracker.dto.request.CreateChallengeRequest;
import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ResourceType;
import com.agtual.challengetracker.exception.AlreadyExistsException;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.ChallengeRepo;

@Service
@lombok.RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeParticipantService challengeParticipantService;
    private final ChallengeRepo challengeRepo;

    @Transactional
    public Challenge createChallenge(User user, CreateChallengeRequest challengeRequest) {
        // challenge owner must not have challenge of same name
        Optional<Challenge> existingChallenge = challengeRepo.findByOwnerAndName(user, challengeRequest.name());

        if (existingChallenge.isPresent()) {
            throw new AlreadyExistsException(ResourceType.CHALLENGE, existingChallenge.get().getId());
        }

        Challenge challenge = challengeRepo.save(new Challenge(challengeRequest, user));

        challengeParticipantService.addOwnerToChallenge(user, challenge);

        return challenge;
    }

    public Challenge getChallenge(User user, Long challengeId) {
        Challenge challenge = challengeRepo.findById(challengeId)
                .orElseThrow(() -> new NotFoundException(ResourceType.CHALLENGE, challengeId));

        if (!challengeParticipantService.isParticipant(user, challenge)) {
            // Throw not found instead of unauthorized
            throw new NotFoundException(ResourceType.CHALLENGE, challengeId);
        }

        return challenge;
    }

}
