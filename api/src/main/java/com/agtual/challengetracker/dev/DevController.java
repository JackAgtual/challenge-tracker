package com.agtual.challengetracker.dev;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping("/dev")
@Profile("dev")
@lombok.RequiredArgsConstructor
public class DevController {

    private final DevDataSeeder seeder;

    @PostMapping("/seed")
    public void seed() {
        seeder.dropDbs();
        seeder.run();
    }
}
