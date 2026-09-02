package com.agtual.challengetracker.service;

import java.util.Optional;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.agtual.challengetracker.dto.request.CreateUserRequest;
import com.agtual.challengetracker.dto.request.UserAccountSetupRequest;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ResourceType;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.UserRepo;

@Service
@lombok.RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public Optional<User> getUser(Jwt jwt) {
        return userRepo.findByAuthSubject(jwt.getSubject());
    }

    public User getValidUser(Jwt jwt) {
        Optional<User> user = getUser(jwt);
        if (user.isEmpty()) {
            throw new NotFoundException(ResourceType.USER, "authSubject", jwt.getSubject());
        }
        return user.get();
    }

    public User getValidUser(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResourceType.USER, "username", username));
    }

    public User createUser(Jwt jwt, CreateUserRequest userRequest) {
        String subject = jwt.getSubject();

        Optional<User> user = userRepo.findByAuthSubject(subject);
        if (user.isPresent()) {
            // Return existing user if there is one
            return user.get();
        }
        return userRepo.save(new User(subject, userRequest));
    }

    public User finishAccountSetup(User user, UserAccountSetupRequest accountSetupRequest) {
        user.setupAccount(accountSetupRequest);
        return userRepo.save(user);
    }
}
