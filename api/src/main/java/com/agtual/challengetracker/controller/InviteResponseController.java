package com.agtual.challengetracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agtual.challengetracker.controller.resolver.CurrentUser;
import com.agtual.challengetracker.dto.response.PendingInvitesForUserResponse;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.service.InviteService;

@RestController
@RequestMapping("/invites")
@lombok.RequiredArgsConstructor
public class InviteResponseController {

    private final InviteService inviteService;

    @GetMapping
    public List<PendingInvitesForUserResponse> getAllPendingInvitesForUser(@CurrentUser User user) {
        return inviteService.getPendingInvites(user)
                .stream()
                .map(PendingInvitesForUserResponse::from)
                .toList();
    }

    @PostMapping("/{inviteId}/accept")
    public void acceptInvite(@CurrentUser User user, @PathVariable Long inviteId) {
        inviteService.acceptInvite(user, inviteId);
    }

    @PostMapping("/{inviteId}/decline")
    public void declineInvite(@CurrentUser User user, @PathVariable Long inviteId) {
        inviteService.declineInvite(user, inviteId);
    }
}
