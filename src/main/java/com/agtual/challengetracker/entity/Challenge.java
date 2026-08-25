package com.agtual.challengetracker.entity;

import java.time.LocalDate;

import com.agtual.challengetracker.dto.request.CreateChallengeRequest;
import com.agtual.challengetracker.dto.request.ModifyChallengeRequest;
import com.agtual.challengetracker.enums.ChallengeStatus;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "challenges", uniqueConstraints = @UniqueConstraint(columnNames = { "owner_id", "name" }))
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "duration_days")
    private Integer durationDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "challenge_status", nullable = false)
    private ChallengeStatus status = ChallengeStatus.PENDING;

    public Challenge(CreateChallengeRequest createChallengeRequest, User user) {
        this.owner = user;
        this.name = createChallengeRequest.name();
        this.durationDays = createChallengeRequest.durationDays();
    }

    public void update(ModifyChallengeRequest modifyChallengeRequest) {
        this.name = modifyChallengeRequest.name();
        this.durationDays = modifyChallengeRequest.durationDays();
    }

    public boolean isReadyToStart() {
        return durationDays != null && name != null && status == ChallengeStatus.PENDING;
    }

    public boolean isConfigurable() {
        return status == ChallengeStatus.PENDING;
    }
}
