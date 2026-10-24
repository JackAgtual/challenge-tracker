package com.agtual.challengetracker.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.Avatar;

public interface AvatarRepo extends JpaRepository<Avatar, String> {

}
