package com.agtual.challengetracker.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.agtual.challengetracker.dto.response.GoalCompletionRowResponse;
import com.agtual.challengetracker.dto.response.GoalDefinitionResponse;
import com.agtual.challengetracker.dto.response.GoalTableResponse;
import com.agtual.challengetracker.entity.Challenge;
import com.agtual.challengetracker.entity.GoalCompletion;
import com.agtual.challengetracker.entity.GoalDefinition;
import com.agtual.challengetracker.enums.ChallengeStatus;
import com.agtual.challengetracker.exception.ForbiddenException;

public class GridUtil {
    /**
     * @param challenge
     * @param goalDefinitions
     * @param goalCompletions sorted in descending order
     * @return
     */
    public static GoalTableResponse createGoalCompletionTable(Challenge challenge, List<GoalDefinition> goalDefinitions,
            List<GoalCompletion> goalCompletions) {
        if (challenge.getStatus().equals(ChallengeStatus.PENDING)) {
            throw new ForbiddenException("Cannot create goal completion table for pending challenge");
        }

        // For each date store a list of completions for each goal
        Map<LocalDate, List<GoalCompletion>> dateToGoalCompletions = new LinkedHashMap<>();
        for (GoalCompletion completion : goalCompletions) {
            dateToGoalCompletions
                    .computeIfAbsent(completion.getCompletedDate(), k -> new ArrayList<>())
                    .add(completion);
        }

        LocalDate mostRecentDate = challenge.getMostRecentDate();

        // Start with all goals completed set to false and populate ones that are true
        Map<Long, Long> allFalseGoalCompletions = new HashMap<>(goalDefinitions.size());
        goalDefinitions.forEach(def -> allFalseGoalCompletions.put(def.getId(), null));

        Iterator<GoalCompletion> goalCompletionIter = goalCompletions.iterator();
        GoalCompletion curCompletion = goalCompletionIter.hasNext() ? goalCompletionIter.next() : null;

        Long daysInChallenge = ChronoUnit.DAYS.between(challenge.getStartDate(), mostRecentDate);
        List<GoalCompletionRowResponse> rows = new ArrayList<>(daysInChallenge.intValue());
        boolean checkedAllCompletions = false;

        for (LocalDate date = mostRecentDate; !date.isBefore(challenge.getStartDate()); date = date.minusDays(1L)) {
            Map<Long, Long> completionsMap = new HashMap<>(allFalseGoalCompletions);

            while (!checkedAllCompletions && curCompletion != null && curCompletion.getCompletedDate().equals(date)) {
                completionsMap.put(curCompletion.getGoalDefinition().getId(), curCompletion.getId());

                if (!goalCompletionIter.hasNext()) {
                    checkedAllCompletions = true;
                    break;
                }

                curCompletion = goalCompletionIter.next();
            }
            rows.add(new GoalCompletionRowResponse(date, completionsMap));
        }

        List<GoalDefinitionResponse> goalDefResponses = goalDefinitions.stream().map(GoalDefinitionResponse::from)
                .toList();
        return new GoalTableResponse(goalDefResponses, rows);
    }
}
