package com.agtual.challengetracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestConfg.class)
class ChallengeTrackerApplicationTests {

	@Test
	void contextLoads() {
	}

}
