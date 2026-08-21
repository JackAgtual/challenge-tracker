package com.agtual.challengetracker.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.ChallengeParticipant;
import com.agtual.challengetracker.entity.User;
import java.util.List;

public interface ChallengeParticipantRepo extends JpaRepository<ChallengeParticipant, Long> {

    Optional<ChallengeParticipant> findByParticipantAndChallenge(User participant, Challenge challenge);

    boolean existsByChallengeAndParticipant(Challenge challenge, User participant);

    List<ChallengeParticipant> findByChallenge(Challenge challenge);

    List<ChallengeParticipant> findByParticipant(User participant);

    Optional<ChallengeParticipant> findByParticipantAndChallengeId(User participant, Long id);
}
