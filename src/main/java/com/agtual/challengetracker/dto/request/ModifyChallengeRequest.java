package com.agtual.challengetracker.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record ModifyChallengeRequest(@NotEmpty String name, @Nullable @Min(1) Integer durationDays) {

}
