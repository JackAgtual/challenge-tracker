package com.agtual.challengetracker.entity;

import com.agtual.challengetracker.dto.request.CreateUserRequest;
import com.agtual.challengetracker.dto.request.UserAccountSetupRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
public class User {

    @Id
    @Column(name = "auth_subject", nullable = false)
    private String authSubject;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username", unique = true)
    private String username;

    public User(String subject, CreateUserRequest userRequest) {
        this.authSubject = subject;
        this.email = userRequest.email();
    }

    public boolean isAccountSetup() {
        return username != null && !username.isEmpty()
                && firstName != null && !firstName.isEmpty()
                && lastName != null && !lastName.isEmpty();
    }

    public void setupAccount(UserAccountSetupRequest setupRequest) {
        username = setupRequest.username();
        firstName = setupRequest.firstName();
        lastName = setupRequest.lastName();
    }
}
