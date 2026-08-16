package com.agtual.challengetracker.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {

}
