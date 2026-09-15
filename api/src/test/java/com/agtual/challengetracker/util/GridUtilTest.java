package com.agtual.challengetracker.util;

import static com.agtual.challengetracker.testutil.TestEntityFactory.validChallenge;
import static com.agtual.challengetracker.testutil.TestEntityFactory.validGoalCompletion;
import static com.agtual.challengetracker.testutil.TestEntityFactory.validGoalDefinition;
import static com.agtual.challengetracker.testutil.TestEntityFactory.validParticipant;
import static com.agtual.challengetracker.testutil.TestEntityFactory.validUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.agtual.challengetracker.dto.response.GoalCompletionRowResponse;
import com.agtual.challengetracker.dto.response.GoalDefinitionResponse;
import com.agtual.challengetracker.dto.response.GoalTableResponse;
import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.GoalCompletion;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.entity.User;
import com.agtual.challengetracker.enums.ChallengeStatus;
import com.agtual.challengetracker.exception.ForbiddenException;

public class GridUtilTest {

    static GoalDefinition goal1;
    static GoalDefinition goal2;
    static GoalDefinition goal3;
    static Challenge challenge;
    static LocalDate day1;
    static LocalDate day2;
    static LocalDate day3;
    static LocalDate day4;
    static LocalDate day5;
    static Map<Long, Long> noCompletions;

    @BeforeAll
    static void beforeAll() {
        User user = validUser();
        challenge = validChallenge(user, "75 hard");
        challenge.setStatus(ChallengeStatus.COMPLETE);
        LocalDate startDate = LocalDate.of(2026, 5, 10);
        challenge.setStartDate(startDate);
        challenge.setDurationDays(5);
        Participant participant = validParticipant(user, challenge);

        goal1 = validGoalDefinition(participant, "goal1");
        goal1.setId(1L);
        goal2 = validGoalDefinition(participant, "goal2");
        goal2.setId(2L);
        goal3 = validGoalDefinition(participant, "goal3");
        goal3.setId(3L);

        day1 = startDate;
        day2 = startDate.plusDays(1L);
        day3 = startDate.plusDays(2L);
        day4 = startDate.plusDays(3L);
        day5 = startDate.plusDays(4L);

        noCompletions = new HashMap<>();
        noCompletions.put(goal1.getId(), null);
        noCompletions.put(goal2.getId(), null);
        noCompletions.put(goal3.getId(), null);
    }

    @Test
    void testThrowsErrorForPendingChallenge() {
        User user = validUser();
        Challenge challenge = validChallenge(user, "a pending challenge");
        challenge.setStatus(ChallengeStatus.PENDING);
        challenge.setStartDate(LocalDate.now());
        challenge.setDurationDays(30);

        Participant participant = validParticipant(user, challenge);
        GoalDefinition goal = validGoalDefinition(participant, "drink water");
        List<GoalDefinition> goalDefs = List.of(goal);

        List<GoalCompletion> completions = Collections.emptyList();

        assertThrows(ForbiddenException.class,
                () -> GridUtil.createGoalCompletionTable(challenge, goalDefs, completions));
    }

    @Test
    void testCreateGoalCompletionTableOnCompleteChallenge() {
        // Place out of order on purpose
        // reponse should follow same order of inputs
        List<GoalDefinition> goalDefinitions = List.of(goal2, goal1, goal3);

        GoalCompletion goal1Day1 = validGoalCompletion(goal1, day1);
        goal1Day1.setId(1L);
        GoalCompletion goal1Day3 = validGoalCompletion(goal1, day3);
        goal1Day3.setId(2L);
        GoalCompletion goal1Day4 = validGoalCompletion(goal1, day4);
        goal1Day4.setId(3L);
        GoalCompletion goal2Day1 = validGoalCompletion(goal2, day1);
        goal2Day1.setId(4L);
        GoalCompletion goal3Day3 = validGoalCompletion(goal3, day3);
        goal3Day3.setId(5L);
        GoalCompletion goal3Day4 = validGoalCompletion(goal3, day4);
        goal3Day4.setId(6L);

        // Completions must be in descending order by date
        List<GoalCompletion> goalCompletions = List.of(
                goal1Day4,
                goal3Day4,
                goal1Day3,
                goal3Day3,
                goal1Day1,
                goal2Day1);

        GoalTableResponse res = GridUtil.createGoalCompletionTable(challenge, goalDefinitions, goalCompletions);

        // Goal definitions
        List<GoalDefinitionResponse> goalDefRes = res.goalDefinitionResponse();
        assertEquals(3, goalDefRes.size());
        assertEquals(List.of("goal2", "goal1", "goal3"), goalDefRes.stream().map(g -> g.name()).toList());

        // goal completions rows
        List<GoalCompletionRowResponse> rowResponses = res.goalCompletionRowResponses();

        Map<Long, Long> day1Completions = new HashMap<>();
        day1Completions.put(goal1.getId(), goal1Day1.getId());
        day1Completions.put(goal2.getId(), goal2Day1.getId());
        day1Completions.put(goal3.getId(), null);

        Map<Long, Long> day2Completions = new HashMap<>(noCompletions);

        Map<Long, Long> day3Completions = new HashMap<>();
        day3Completions.put(goal1.getId(), goal1Day3.getId());
        day3Completions.put(goal2.getId(), null);
        day3Completions.put(goal3.getId(), goal3Day3.getId());

        Map<Long, Long> day4Completions = new HashMap<>();
        day4Completions.put(goal1.getId(), goal1Day4.getId());
        day4Completions.put(goal2.getId(), null);
        day4Completions.put(goal3.getId(), goal3Day4.getId());

        Map<Long, Long> day5Completions = new HashMap<>(noCompletions);

        // Rows must go in date descending order
        GoalCompletionRowResponse row1 = new GoalCompletionRowResponse(day5, day5Completions);
        GoalCompletionRowResponse row2 = new GoalCompletionRowResponse(day4, day4Completions);
        GoalCompletionRowResponse row3 = new GoalCompletionRowResponse(day3, day3Completions);
        GoalCompletionRowResponse row4 = new GoalCompletionRowResponse(day2, day2Completions);
        GoalCompletionRowResponse row5 = new GoalCompletionRowResponse(day1, day1Completions);

        assertAll(
                () -> assertEquals(row1, rowResponses.get(0)),
                () -> assertEquals(row2, rowResponses.get(1)),
                () -> assertEquals(row3, rowResponses.get(2)),
                () -> assertEquals(row4, rowResponses.get(3)),
                () -> assertEquals(row5, rowResponses.get(4)));
    }

    @Test
    void testCreateGoalCompletionTableWithNoCompletions() {
        List<GoalDefinition> goalDefinitions = List.of(goal1, goal2, goal3);
        List<GoalCompletion> goalCompletions = List.of();

        GoalTableResponse res = GridUtil.createGoalCompletionTable(challenge, goalDefinitions, goalCompletions);
        List<GoalCompletionRowResponse> rowResponses = res.goalCompletionRowResponses();

        // Rows must go in date descending order
        GoalCompletionRowResponse row1 = new GoalCompletionRowResponse(day5, noCompletions);
        GoalCompletionRowResponse row2 = new GoalCompletionRowResponse(day4, noCompletions);
        GoalCompletionRowResponse row3 = new GoalCompletionRowResponse(day3, noCompletions);
        GoalCompletionRowResponse row4 = new GoalCompletionRowResponse(day2, noCompletions);
        GoalCompletionRowResponse row5 = new GoalCompletionRowResponse(day1, noCompletions);

        assertAll(
                () -> assertEquals(row1, rowResponses.get(0)),
                () -> assertEquals(row2, rowResponses.get(1)),
                () -> assertEquals(row3, rowResponses.get(2)),
                () -> assertEquals(row4, rowResponses.get(3)),
                () -> assertEquals(row5, rowResponses.get(4)));
    }
}
