package com.agtual.challengetracker.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.Challenge;

public interface ChallengeRepo extends JpaRepository<Challenge, Long> {

}
