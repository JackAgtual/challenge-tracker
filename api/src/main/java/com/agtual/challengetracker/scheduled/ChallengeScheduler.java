package com.agtual.challengetracker.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.agtual.challengetracker.service.ChallengeService;

@Component
@lombok.RequiredArgsConstructor
public class ChallengeScheduler {
    private final ChallengeService challengeService;

    @Scheduled(cron = "30 0 0 * * *", zone = "America/Los_Angeles")
    public void completeAllEligibleChallenges() {
        challengeService.completeAllEligibleChallenges();
    }
}
