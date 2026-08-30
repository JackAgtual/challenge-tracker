package com.agtual.challengetracker.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record CompleteGoalRequest(@NotNull @PastOrPresent LocalDate date) {

}
