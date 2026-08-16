package com.agtual.challengetracker.service;

import java.util.Optional;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.agtual.challengetracker.dto.request.UserRequest;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.repo.UserRepo;

@Service
@lombok.RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public Optional<User> getUser(Jwt jwt) {
        return userRepo.findByAuthSubject(jwt.getSubject());
    }

    public User createUser(Jwt jwt, UserRequest userRequest) {
        String subject = jwt.getSubject();
        if (userRepo.findByAuthSubject(subject).isPresent()) {
            throw new RuntimeException("User already exists"); // FIXME
        }
        return userRepo.save(new User(subject, userRequest));
    }
}
