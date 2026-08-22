package com.agtual.challengetracker.repo;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.GoalCompletion;
import com.agtual.challengetracker.entity.GoalDefinition;

public interface GoalCompletionRepo extends JpaRepository<GoalCompletion, Long> {
    Optional<GoalCompletion> findByGoalDefinitionAndCompletedDate(GoalDefinition goalDefinition,
            LocalDate completedDate);
}