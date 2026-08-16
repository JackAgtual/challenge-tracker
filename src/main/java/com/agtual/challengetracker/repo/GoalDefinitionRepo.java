package com.agtual.challengetracker.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agtual.challengetracker.entity.GoalDefinition;

public interface GoalDefinitionRepo extends JpaRepository<GoalDefinition, Long> {

}
