package com.agtual.challengetracker.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.entity.User;
import java.util.List;

public interface ParticipantRepo extends JpaRepository<Participant, Long> {

    Optional<Participant> findByUserAndChallenge(User user, Challenge challenge);

    Optional<Participant> findByUserAndChallengeId(User user, Long challengeId);

    Optional<Participant> findByIdAndChallengeId(Long participantId, Long challengeId);

    List<Participant> findByChallengeId(Long challengeId);

    List<Participant> findByUser(User user);

    boolean existsByUserAndChallengeId(User user, Long challengeId);
}
