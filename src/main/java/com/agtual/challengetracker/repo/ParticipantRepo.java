package com.agtual.challengetracker.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.entity.User;
import java.util.List;

public interface ParticipantRepo extends JpaRepository<Participant, Long> {

    Optional<Participant> findByUserAndChallenge(User user, Challenge challenge);

    boolean existsByChallengeAndUser(Challenge challenge, User user);

    List<Participant> findByChallenge(Challenge challenge);

    List<Participant> findByUser(User user);

    Optional<Participant> findByUserAndChallengeId(User user, Long challengeId);
}
