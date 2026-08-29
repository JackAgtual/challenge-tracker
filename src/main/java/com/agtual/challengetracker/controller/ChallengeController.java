package com.agtual.challengetracker.controller;

import com.agtual.challengetracker.service.InviteService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agtual.challengetracker.controller.resolver.CurrentUser;
import com.agtual.challengetracker.dto.request.CreateChallengeRequest;
import com.agtual.challengetracker.dto.request.ModifyChallengeRequest;
import com.agtual.challengetracker.dto.request.SendInviteRequest;
import com.agtual.challengetracker.dto.response.ChallengeResponse;
import com.agtual.challengetracker.dto.response.IdResponse;
import com.agtual.challengetracker.dto.response.NonAcceptedInvitesForChallengeResponse;
import com.agtual.challengetracker.dto.response.ReadyRequest;
import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.service.ChallengeService;
import com.agtual.challengetracker.service.ParticipantService;
import com.agtual.challengetracker.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/challenges")
@lombok.RequiredArgsConstructor
public class ChallengeController {

    private final InviteService inviteService;
    private final ChallengeService challengeService;
    private final ParticipantService participantService;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse createChallenge(@CurrentUser User user,
            @Valid @RequestBody CreateChallengeRequest createChallengeRequest) {
        Challenge challenge = challengeService.createChallenge(user, createChallengeRequest);
        return new IdResponse(challenge.getId());
    }

    @PostMapping("/{challengeId}/start")
    public void startChallenge(@CurrentUser User user, @PathVariable Long challengeId) {
        challengeService.startChallenge(user, challengeId);
    }

    @PutMapping("/{challengeId}")
    public ChallengeResponse modifyChallenge(@CurrentUser User user, @PathVariable Long challengeId,
            @Valid @RequestBody ModifyChallengeRequest modifications) {
        Challenge challenge = challengeService.modifyChallenge(user, challengeId, modifications);
        return ChallengeResponse.from(challenge);
    }

    @PostMapping("/{challengeId}/ready")
    public void setReady(@CurrentUser User user, @PathVariable Long challengeId,
            @Valid @RequestBody ReadyRequest readyRequest) {
        participantService.setReady(user, challengeId, readyRequest.ready());
    }

    @DeleteMapping("/{challengeId}/participants/{participantId}")
    public void ownerRemoveParticipantFromChallenge(@CurrentUser User user, @PathVariable Long challengeId,
            @PathVariable Long participantId) {
        participantService.ownerRemovesParticipantFromChallenge(user, challengeId, participantId);
    }

    @DeleteMapping("/{challengeId}/leave")
    public void participantLeavesChallenge(@CurrentUser User user, @PathVariable Long challengeId) {
        participantService.leaveChallenge(user, challengeId);
    }

    @PostMapping("/{challengeId}/invites")
    public void inviteUserToChallenge(@CurrentUser User user, @PathVariable Long challengeId,
            @Valid @RequestBody SendInviteRequest sendInviteRequest) {
        User userToInvite = userService.getValidUser(sendInviteRequest.username());
        inviteService.inviteToChallenge(user, challengeId, userToInvite);
    }

    @GetMapping("/{challengeId}/invites")
    public List<NonAcceptedInvitesForChallengeResponse> getNonAcceptedInvitesForChallenge(@CurrentUser User user,
            @PathVariable Long challengeId) {
        return inviteService.getNonAcceptedInvitesForChallenge(user, challengeId)
                .stream()
                .map(NonAcceptedInvitesForChallengeResponse::from)
                .toList();
    }
}
