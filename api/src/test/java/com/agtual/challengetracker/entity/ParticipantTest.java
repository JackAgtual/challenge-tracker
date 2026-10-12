package com.agtual.challengetracker.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ParticipantTest {
    @Test
    void testEligibleToSetReady() {
        Participant participant = new Participant();
        participant.setGoalDefinitions(List.of(new GoalDefinition()));

        assertTrue(participant.isEligibleToSetReady());
    }

    @Test
    void testNotEligibleToSetReady() {
        Participant participant = new Participant();
        participant.setGoalDefinitions(Collections.emptyList());

        assertFalse(participant.isEligibleToSetReady());
    }
}