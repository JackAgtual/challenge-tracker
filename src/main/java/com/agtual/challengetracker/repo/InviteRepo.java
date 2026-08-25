package com.agtual.challengetracker.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.Invite;
import com.agtual.challengetracker.entity.User;

public interface InviteRepo extends JpaRepository<Invite, Long> {
    Optional<Invite> findByChallengeAndInvitedUser(Challenge challenge, User invitedUser);

    Optional<Invite> findByIdAndInvitedUser(Long id, User invitedUser);
}
