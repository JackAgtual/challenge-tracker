package com.agtual.challengetracker.enums;

@lombok.RequiredArgsConstructor
@lombok.Getter
public enum ResourceType {
    USER("User"),
    CHALLENGE("Challenge"),
    CHALLENGE_PARTICIPANT("Challenge participant");

    private final String displayName;
}
