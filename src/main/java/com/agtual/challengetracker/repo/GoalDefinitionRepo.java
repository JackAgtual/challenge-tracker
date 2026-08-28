package com.agtual.challengetracker.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.entity.User;

public interface GoalDefinitionRepo extends JpaRepository<GoalDefinition, Long> {

    List<GoalDefinition> findByParticipant_UserAndParticipant_Challenge_Id(User user, Long challengeId);
}
