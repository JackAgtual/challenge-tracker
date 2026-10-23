package com.agtual.challengetracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.agtual.challengetracker.dto.request.CreateUserRequest;
import com.agtual.challengetracker.dto.request.UserAccountSetupRequest;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ResourceType;
import com.agtual.challengetracker.exception.AlreadyExistsException;
import com.agtual.challengetracker.exception.NotFoundException;
import com.agtual.challengetracker.repo.AvatarRepo;
import com.agtual.challengetracker.repo.UserRepo;
import com.agtual.challengetracker.util.QueryUtil;

@Service
@lombok.RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final AvatarRepo avatarRepo;

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
        if (userRepo.existsByUsernameAndAuthSubjectNot(accountSetupRequest.username(), user.getAuthSubject())) {
            throw new AlreadyExistsException(ResourceType.USER, accountSetupRequest.username());
        }

        if (accountSetupRequest.avatarSrc() != null && !avatarRepo.existsById(accountSetupRequest.avatarSrc())) {
            throw new NotFoundException(ResourceType.AVATAR, accountSetupRequest.avatarSrc());
        }

        user.setupAccount(accountSetupRequest);
        return userRepo.save(user);
    }

    public List<User> searchUser(String query) {
        return userRepo.searchUser(QueryUtil.buildQuery(query)).stream()
                .filter(user -> user.isAccountSetup()).toList();
    }
}
