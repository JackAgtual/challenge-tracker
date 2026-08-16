package com.agtual.challengetracker.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.ChallengeParticipant;

public interface ChallengeParticipantRepo extends JpaRepository<ChallengeParticipant, Long> {

}
