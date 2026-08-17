package com.agtual.challengetracker.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.ChallengeParticipant;
import com.agtual.challengetracker.entity.User;

public interface ChallengeParticipantRepo extends JpaRepository<ChallengeParticipant, Long> {

    Optional<ChallengeParticipant> findByParticipantAndChallenge(User participant, Challenge challenge);
}
