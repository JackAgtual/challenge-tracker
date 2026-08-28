package com.agtual.challengetracker.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.GoalCompletion;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.entity.User;

public interface GoalCompletionRepo extends JpaRepository<GoalCompletion, Long> {
    Optional<GoalCompletion> findByGoalDefinitionAndCompletedDate(GoalDefinition goalDefinition,
            LocalDate completedDate);

    List<GoalCompletion> findByGoalDefinition_Participant_Challenge_IdAndGoalDefinition_Participant_User(
            Long challengeId, User user);
}