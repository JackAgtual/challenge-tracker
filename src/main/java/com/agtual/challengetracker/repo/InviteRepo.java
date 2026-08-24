package com.agtual.challengetracker.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.Invite;

public interface InviteRepo extends JpaRepository<Invite, Long> {
    Optional<Invite> findByChallengeAndInvitedUserEmail(Challenge challenge, String invitedUserEmail);
}
