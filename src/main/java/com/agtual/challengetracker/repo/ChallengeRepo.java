package com.agtual.challengetracker.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.User;

public interface ChallengeRepo extends JpaRepository<Challenge, Long> {

    Optional<Challenge> findByOwnerAndName(User owner, String name);
}
