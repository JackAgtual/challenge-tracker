package com.agtual.challengetracker.entity;

import com.agtual.challengetracker.dto.request.UserRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auth_subject", unique = true)
    private String authSubject;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    public User(String subject, UserRequest userRequest) {
        this.authSubject = subject;
        this.email = userRequest.email();
        this.firstName = userRequest.firstName();
        this.lastName = userRequest.lastName();
    }
}
