package com.agtual.challengetracker.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "goal_definitions", uniqueConstraints = @UniqueConstraint(columnNames = { "participant_id",
        "name" }))
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
public class GoalDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "goalDefinition", cascade = CascadeType.ALL)
    private List<GoalCompletion> goalCompletions = new ArrayList<>();
}
