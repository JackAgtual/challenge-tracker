package com.agtual.challengetracker.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "goal_completions", uniqueConstraints = @UniqueConstraint(columnNames = { "goal_definition_id",
        "completed_date" }))
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
public class GoalCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_definition_id", nullable = false)
    private GoalDefinition goalDefinition;

    @Column(name = "completed_date", nullable = false)
    private LocalDate completedDate;
}
