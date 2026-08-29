package com.agtual.challengetracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ChallengeStatus;
import com.agtual.challengetracker.enums.ResourceType;
import com.agtual.challengetracker.exception.ForbiddenException;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.ParticipantRepo;

@Service
@lombok.RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepo participantRepo;

    public Participant addUserToChallenge(User user, Challenge challenge) {
        Optional<Participant> existingParticipant = participantRepo.findByUserAndChallenge(user, challenge);
        if (existingParticipant.isPresent()) {
            throw new ForbiddenException(ResourceType.PARTICIPANT, existingParticipant.get().getId(),
                    "Participant already exists in challenge");
        }

        Participant participant = new Participant();
        participant.setUser(user);
        participant.setChallenge(challenge);

        return participantRepo.save(participant);
    }

    public boolean allJoinedParticipantsAreReady(Challenge challenge) {
        List<Participant> participants = participantRepo.findByChallenge(challenge);
        if (participants.isEmpty()) {
            return false;
        }

        return participants.stream()
                .allMatch(participant -> participant.isReady());
    }

    public boolean isParticipant(User user, Challenge challenge) {
        return participantRepo.existsByChallengeAndUser(challenge, user);
    }

    public boolean isParticipant(User user, Long challengeId) {
        return participantRepo.existsByChallenge_IdAndUser(challengeId, user);
    }

    public List<Participant> getAllChallengeParticipationsForUser(User user) {
        return participantRepo.findByUser(user);
    }

    public Participant getChallengeParticipationForUserAndChallengeId(User user, Long challengeId) {
        return participantRepo.findByUserAndChallengeId(user,
                challengeId).orElseThrow(
                        () -> new NotFoundException(ResourceType.PARTICIPANT, "challengeId=" + challengeId));
    }

    public Participant setReady(User user, Long challengeId, boolean ready) {
        Participant participant = getChallengeParticipationForUserAndChallengeId(user, challengeId);
        participant.setReady(ready);

        if (participant.getChallenge().getStatus() != ChallengeStatus.PENDING) {
            throw new ForbiddenException("Can only change ready state when challenge is pending");
        }

        return participantRepo.save(participant);
    }

    public void ownerRemovesParticipantFromChallenge(User challengeOwner, Long participantId) {
        Participant participant = participantRepo
                .findById(participantId)
                .orElseThrow(() -> new NotFoundException(ResourceType.PARTICIPANT, participantId));

        Challenge challenge = participant.getChallenge();
        if (!challenge.getOwner().getId().equals(challengeOwner.getId())) {
            throw new NotFoundException(ResourceType.CHALLENGE, "participant id", participantId);
        }

        removeParticipantFromChallenge(participant, challenge);
    }

    public void leaveChallenge(User user, Long challengeId) {
        Participant participant = participantRepo.findByUserAndChallengeId(user, challengeId)
                .orElseThrow(() -> new NotFoundException(ResourceType.PARTICIPANT, "?"));

        Challenge challenge = participant.getChallenge();
        if (challenge.getOwner().getId().equals(user.getId())) {
            throw new ForbiddenException("Challenge owner can't leave challenge");
        }

        removeParticipantFromChallenge(participant, challenge);
    }

    private void removeParticipantFromChallenge(Participant participant, Challenge challenge) {
        // challenge must be pending
        if (challenge.getStatus() != ChallengeStatus.PENDING) {
            throw new ForbiddenException("Can only remove challenge participant when challenge status is pending");
        }

        participantRepo.delete(participant);
    }

}
