package com.agtual.challengetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChallengeTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeTrackerApplication.class, args);
	}

}
