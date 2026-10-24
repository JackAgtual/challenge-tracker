package com.agtual.challengetracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agtual.challengetracker.dto.response.AvatarResponse;
import com.agtual.challengetracker.repo.AvatarRepo;

@RestController
@RequestMapping("/icons")
@lombok.RequiredArgsConstructor
public class IconController {

    private final AvatarRepo avatarRepo;

    @GetMapping("/avatars")
    public List<AvatarResponse> getAllAvatars() {
        return avatarRepo.findAll().stream()
                .map(avatar -> new AvatarResponse(avatar.getSrc())).toList();
    }

}
