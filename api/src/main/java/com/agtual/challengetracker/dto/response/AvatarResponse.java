package com.agtual.challengetracker.dto.response;

import jakarta.validation.constraints.NotBlank;

public record AvatarResponse(@NotBlank String src) {

}
