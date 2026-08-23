package com.agtual.challengetracker.entity;

import java.util.ArrayList;
import java.util.List;

import com.agtual.challengetracker.enums.InviteStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "participants", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id",
        "challenge_id" }))
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Enumerated(EnumType.STRING)
    @Column(name = "invite_status", nullable = false)
    private InviteStatus inviteStatus = InviteStatus.PENDING;

    @Column(name = "ready", nullable = false)
    private boolean ready = false;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    private List<GoalDefinition> goalDefinitions = new ArrayList<>();
}
