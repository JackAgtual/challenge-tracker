package com.agtual.challengetracker.service;

import java.util.Optional;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.agtual.challengetracker.dto.request.UserRequest;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ResourceType;
import com.agtual.challengetracker.exception.AlreadyExistsException;
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

        Optional<User> user = userRepo.findByAuthSubject(subject);
        if (user.isPresent()) {
            throw new AlreadyExistsException(ResourceType.USER, user.get().getId());
        }
        return userRepo.save(new User(subject, userRequest));
    }
}
