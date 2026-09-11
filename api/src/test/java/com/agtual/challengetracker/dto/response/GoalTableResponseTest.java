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

        GoalCompletion goal1Day1 = validGoalCompletion(goal1, day1);
        goal1Day1.setId(1L);
        GoalCompletion goal1Day2 = validGoalCompletion(goal1, day2);
        goal1Day2.setId(2L);
        GoalCompletion goal1Day3 = validGoalCompletion(goal1, day3);
        goal1Day3.setId(3L);
        GoalCompletion goal2Day1 = validGoalCompletion(goal2, day1);
        goal2Day1.setId(4L);
        GoalCompletion goal3Day2 = validGoalCompletion(goal3, day2);
        goal3Day2.setId(5L);
        GoalCompletion goal3Day3 = validGoalCompletion(goal3, day3);
        goal3Day3.setId(6L);
        List<GoalCompletion> goalCompletions = List.of(
                goal1Day1,
                goal1Day2,
                goal1Day3,
                goal2Day1,
                goal3Day2,
                goal3Day3);

        GoalTableResponse res = GoalTableResponse.from(goalDefinitions, goalCompletions);

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

        Map<Long, Long> day2Completions = new HashMap<>();
        day2Completions.put(goal1.getId(), goal1Day2.getId());
        day2Completions.put(goal2.getId(), null);
        day2Completions.put(goal3.getId(), goal3Day2.getId());

        Map<Long, Long> day3Completions = new HashMap<>();
        day3Completions.put(goal1.getId(), goal1Day3.getId());
        day3Completions.put(goal2.getId(), null);
        day3Completions.put(goal3.getId(), goal3Day3.getId());

        GoalCompletionRowResponse row1 = new GoalCompletionRowResponse(day1, day1Completions);
        GoalCompletionRowResponse row2 = new GoalCompletionRowResponse(day2, day2Completions);
        GoalCompletionRowResponse row3 = new GoalCompletionRowResponse(day3, day3Completions);

        assertEquals(rowResponses, List.of(row1, row2, row3));
    }

}
