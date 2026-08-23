package com.agtual.challengetracker.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agtual.challengetracker.dto.request.CreateChallengeRequest;
import com.agtual.challengetracker.dto.request.ModifyChallengeRequest;
import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ChallengeStatus;
import com.agtual.challengetracker.enums.ResourceType;
import com.agtual.challengetracker.exception.AlreadyExistsException;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.ChallengeRepo;

@Service
@lombok.RequiredArgsConstructor
public class ChallengeService {

    private final ParticipantService participantService;
    private final ChallengeRepo challengeRepo;

    @Transactional
    public Challenge createChallenge(User user, CreateChallengeRequest challengeRequest) {
        // challenge owner must not have challenge of same name
        Optional<Challenge> existingChallenge = challengeRepo.findByOwnerAndName(user, challengeRequest.name());

        if (existingChallenge.isPresent()) {
            throw new AlreadyExistsException(ResourceType.CHALLENGE, existingChallenge.get().getId());
        }

        Challenge challenge = challengeRepo.save(new Challenge(challengeRequest, user));

        participantService.addOwnerToChallenge(user, challenge);

        return challenge;
    }

    public Challenge getChallenge(User user, Long challengeId) {
        Challenge challenge = challengeRepo.findById(challengeId)
                .orElseThrow(() -> new NotFoundException(ResourceType.CHALLENGE, challengeId));

        if (!participantService.isParticipant(user, challenge)) {
            // Throw not found instead of unauthorized
            throw new NotFoundException(ResourceType.CHALLENGE, challengeId);
        }

        return challenge;
    }

    /**
     * Will update challenge that belongs to a user
     * Will set all values in modifyChallengeRequest even if null
     * Only allowed to modify challenges in pending state (not in progress or
     * complete)
     * 
     * @param user
     * @param challengeId
     * @param modifyChallengeRequest
     * @return
     */
    public Challenge modifyChallenge(User user, Long challengeId, ModifyChallengeRequest modifyChallengeRequest) {
        Challenge challenge = challengeRepo.findByOwnerAndId(user, challengeId)
                .orElseThrow(() -> new NotFoundException(ResourceType.CHALLENGE, challengeId));

        if (challenge.getStatus() != ChallengeStatus.PENDING) {
            throw new ForbiddenException(ResourceType.CHALLENGE, challenge.getId(),
                    "Can only modify challenge during PENDING status");
        }

        challenge.update(modifyChallengeRequest);
        return challengeRepo.save(challenge);
    }

    public Challenge startChallenge(User user, Long challengeId) {
        Challenge challenge = challengeRepo.findByOwnerAndId(user, challengeId)
                .orElseThrow(() -> new NotFoundException(ResourceType.CHALLENGE, challengeId));

        if (!challenge.isReadyToStart()) {
            throw new ForbiddenException(ResourceType.CHALLENGE, challengeId, "Challenge start conditions not met.");
        }

        if (!participantService.allJoinedParticipantsAreReady(challenge)) {
            throw new ForbiddenException(ResourceType.CHALLENGE, challengeId, "Not all challenge participants ready.");
        }

        challenge.setStatus(ChallengeStatus.IN_PROGRESS);
        challenge.setStartDate(LocalDate.now());
        return challengeRepo.save(challenge);
    }
}
