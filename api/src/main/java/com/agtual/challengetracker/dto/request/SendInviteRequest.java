package com.agtual.challengetracker.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SendInviteRequest(@NotBlank String username) {

}
