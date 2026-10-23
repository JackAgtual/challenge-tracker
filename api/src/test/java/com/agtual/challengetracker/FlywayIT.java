package com.agtual.challengetracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.agtual.challengetracker.repo.AvatarRepo;

@SpringBootTest(properties = { "spring.flyway.enabled=true",
        "spring.jpa.hibernate.ddl-auto=validate" })
@Testcontainers
public class FlywayIT {
    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    AvatarRepo avatarRepo;

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18.6");

    @Test
    void testFlywayRuns() {
        Integer failed = jdbc.queryForObject(
                "SELECT count(*) FROM flyway_schema_history WHERE success = false", Integer.class);
        Integer applied = jdbc.queryForObject(
                "SELECT count(*) FROM flyway_schema_history WHERE success = true", Integer.class);

        assertEquals(0, failed);
        assertTrue(applied > 0);
    }

    @Test
    void testFlywayInsertsAvatars() {
        assertTrue(avatarRepo.count() > 0);
    }
}
