package com.agtual.challengetracker.util;

import java.util.List;
import java.util.stream.Collectors;

public class QueryUtil {
    private static final String prefixMatch = ":*";
    private static final String inputDelimiter = " ";
    private static final String queryDelimiter = " & ";

    public static String buildQuery(String query) {
        if (query.length() == 0) {
            return "";
        }

        return List.of(query.split(inputDelimiter)).stream()
                .map(q -> q + prefixMatch)
                .collect(Collectors.joining(queryDelimiter));
    }
}
