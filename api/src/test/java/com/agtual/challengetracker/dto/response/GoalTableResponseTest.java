package com.agtual.challengetracker.dto.response;

import static com.agtual.challengetracker.testutil.TestEntityFactory.validChallenge;
import static com.agtual.challengetracker.testutil.TestEntityFactory.validGoalCompletion;
import static com.agtual.challengetracker.testutil.TestEntityFactory.validGoalDefinition;
import static com.agtual.challengetracker.testutil.TestEntityFactory.validParticipant;
import static com.agtual.challengetracker.testutil.TestEntityFactory.validUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.GoalCompletion;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.entity.Participant;
import com.agtual.challengetracker.entity.User;

public class GoalTableResponseTest {
    @Test
    void testFrom() {
        User user = validUser();
        Challenge challenge = validChallenge(user, "75 hard");
        Participant participant = validParticipant(user, challenge);

        GoalDefinition goal1 = validGoalDefinition(participant, "goal1");
        goal1.setId(1L);
        GoalDefinition goal2 = validGoalDefinition(participant, "goal2");
        goal2.setId(2L);
        GoalDefinition goal3 = validGoalDefinition(participant, "goal3");
        goal3.setId(3L);

        // Place out of order on purpose
        // reponse should follow same order of inputs
        List<GoalDefinition> goalDefinitions = List.of(goal2, goal1, goal3);

        LocalDate day1 = LocalDate.now();
        LocalDate day2 = day1.minusDays(1);
        LocalDate day3 = day2.minusDays(1);

        List<GoalCompletion> goalCompletions = List.of(
                validGoalCompletion(goal1, day1),
                validGoalCompletion(goal1, day2),
                validGoalCompletion(goal1, day3),
                validGoalCompletion(goal2, day1),
                validGoalCompletion(goal3, day2),
                validGoalCompletion(goal3, day3));

        GoalTableResponse res = GoalTableResponse.from(goalDefinitions, goalCompletions);

        // Goal definitions
        List<GoalDefinitionResponse> goalDefRes = res.goalDefinitionResponse();
        assertEquals(3, goalDefRes.size());
        assertEquals(List.of("goal2", "goal1", "goal3"), goalDefRes.stream().map(g -> g.name()).toList());

        // goal completions rows
        List<GoalCompletionRowResponse> rowResponses = res.goalCompletionRowResponses();
        Map<Long, Boolean> day1Completions = new HashMap<>();
        day1Completions.put(goal1.getId(), Boolean.TRUE);
        day1Completions.put(goal2.getId(), Boolean.TRUE);
        day1Completions.put(goal3.getId(), Boolean.FALSE);

        Map<Long, Boolean> day2Completions = new HashMap<>();
        day2Completions.put(goal1.getId(), Boolean.TRUE);
        day2Completions.put(goal2.getId(), Boolean.FALSE);
        day2Completions.put(goal3.getId(), Boolean.TRUE);

        Map<Long, Boolean> day3Completions = new HashMap<>();
        day3Completions.put(goal1.getId(), Boolean.TRUE);
        day3Completions.put(goal2.getId(), Boolean.FALSE);
        day3Completions.put(goal3.getId(), Boolean.TRUE);

        GoalCompletionRowResponse row1 = new GoalCompletionRowResponse(day1, day1Completions);
        GoalCompletionRowResponse row2 = new GoalCompletionRowResponse(day2, day2Completions);
        GoalCompletionRowResponse row3 = new GoalCompletionRowResponse(day3, day3Completions);

        assertEquals(rowResponses, List.of(row1, row2, row3));
    }

}
