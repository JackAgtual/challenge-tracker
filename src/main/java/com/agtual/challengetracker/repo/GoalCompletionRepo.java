package com.agtual.challengetracker.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.GoalCompletion;

public interface GoalCompletionRepo extends JpaRepository<GoalCompletion, Long> {

}