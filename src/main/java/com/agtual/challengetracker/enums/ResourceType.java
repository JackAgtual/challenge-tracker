package com.agtual.challengetracker.enums;

@lombok.RequiredArgsConstructor
@lombok.Getter
public enum ResourceType {
    USER("User"),
    CHALLENGE("Challenge");

    private final String displayName;
}
