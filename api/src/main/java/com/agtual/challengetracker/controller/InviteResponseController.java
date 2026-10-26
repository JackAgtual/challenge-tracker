package com.agtual.challengetracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agtual.challengetracker.controller.resolver.CurrentUser;
import com.agtual.challengetracker.dto.response.ReceivedInviteResponse;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.service.InviteService;

@RestController
@RequestMapping("/invites")
@lombok.RequiredArgsConstructor
public class InviteResponseController {

    private final InviteService inviteService;

    @GetMapping
    public List<ReceivedInviteResponse> getAllPendingInvitesForUser(@CurrentUser User user) {
        return inviteService.getPendingInvites(user)
                .stream()
                .map(ReceivedInviteResponse::from)
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

    @DeleteMapping("/{inviteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvite(@CurrentUser User user, @PathVariable Long inviteId) {
        inviteService.deleteInvite(user, inviteId);
    }
}
