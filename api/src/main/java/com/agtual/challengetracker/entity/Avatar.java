package com.agtual.challengetracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "avatars")
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
public class Avatar {

    @Id
    @Column(name = "src")
    private String src;
}
