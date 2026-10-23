package com.agtual.challengetracker.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ChallengeStatus;

public interface ChallengeRepo extends JpaRepository<Challenge, Long> {

    // TODO: Ignore case on name
    Optional<Challenge> findByOwnerAndName(User owner, String name);

    Optional<Challenge> findByOwnerAndId(User owner, Long id);

    List<Challenge> findByStatus(ChallengeStatus status);
}
