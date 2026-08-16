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

@Entity
@Table(name = "completed_goals")
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
public class CompletedGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_definition_id", nullable = false)
    private ChallengeParticipant participant;

    @Column(name = "completed_date", nullable = false)
    private LocalDate completedDate;
}
