package com.agtual.challengetracker.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class QueryUtilTest {

    @Test
    void testBuildQuery() {
        String rawQuery = "Jack Jo";
        assertEquals("Jack:* & Jo:*", QueryUtil.buildQuery(rawQuery));
    }

    @Test
    void testBuildQueryHandlesOneWord() {
        String rawQuery = "smit";
        assertEquals("smit:*", QueryUtil.buildQuery(rawQuery));
    }

    @Test
    void testBuildQueryHandlesEmptyQuery() {
        assertEquals("", QueryUtil.buildQuery(""));
    }

}
