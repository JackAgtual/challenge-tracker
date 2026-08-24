package com.agtual.challengetracker.entity;

import java.util.UUID;

import com.agtual.challengetracker.enums.InviteStatus;

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
@Table(name = "invites", uniqueConstraints = @UniqueConstraint(columnNames = { "challenge_id",
        "invited_user_email" }))
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invite_sender_id", nullable = false)
    private User inviteSender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_user_id")
    private User invitedUser;

    // Needed if invitedUser does not have an account
    @Column(name = "invited_user_email", nullable = false)
    private String invitedUserEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InviteStatus status = InviteStatus.PENDING;

    @Column(name = "token", nullable = false)
    private UUID token;
}
