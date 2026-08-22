package com.agtual.challengetracker.enums;

@lombok.RequiredArgsConstructor
@lombok.Getter
public enum ResourceType {
    USER("User"),
    CHALLENGE("Challenge"),
    CHALLENGE_PARTICIPANT("Challenge participant"),
    GOAL_DEFINITION("Goal definition"),
    GOAL_COMPLETION("Goal completion");

    private final String displayName;
}
