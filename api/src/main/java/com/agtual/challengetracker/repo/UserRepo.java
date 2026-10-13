package com.agtual.challengetracker.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agtual.challengetracker.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByAuthSubject(String authSubject);

    boolean existsByUsernameAndAuthSubjectNot(String username, String authSubject);

    @Query(value = """
            SELECT * FROM users
            WHERE search_vector @@ to_tsquery('simple', :query)
            ORDER BY ts_rank(search_vector, to_tsquery('simple', :query)) DESC, username -- username is tiebreaker if ts_rank is equal
            LIMIT 5
            """, nativeQuery = true)
    List<User> searchUser(@Param("query") String query);
}
