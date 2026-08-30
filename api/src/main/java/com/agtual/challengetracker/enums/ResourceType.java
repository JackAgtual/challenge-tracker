package com.agtual.challengetracker.enums;

@lombok.RequiredArgsConstructor
@lombok.Getter
public enum ResourceType {
    USER("User"),
    CHALLENGE("Challenge"),
    PARTICIPANT("Participant"),
    GOAL_DEFINITION("Goal definition"),
    GOAL_COMPLETION("Goal completion"),
    INVITE("Invite");

    private final String displayName;
}
