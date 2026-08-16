package com.agtual.challengetracker.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.CompletedGoal;

public interface CompletedGoalRepo extends JpaRepository<CompletedGoal, Long> {

}