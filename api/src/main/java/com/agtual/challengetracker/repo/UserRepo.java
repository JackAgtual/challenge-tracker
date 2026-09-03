package com.agtual.challengetracker.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.User;

public interface UserRepo extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
