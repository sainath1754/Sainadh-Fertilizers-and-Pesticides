package com.sainadh.fertilizers.repository;

import com.sainadh.fertilizers.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username (used by Spring Security)
    Optional<User> findByUsername(String username);

    // Check if username already taken
    boolean existsByUsername(String username);

    // Check if email already registered
    boolean existsByEmail(String email);

    // Add 1 to failed attempt counter
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.failedAttempts = u.failedAttempts + 1 WHERE u.username = :username")
    void incrementFailedAttempts(String username);

    // Reset counter to 0 after successful login
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.failedAttempts = 0, u.locked = false WHERE u.username = :username")
    void resetFailedAttempts(String username);

    // Lock the account
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.locked = true WHERE u.username = :username")
    void lockAccount(String username);
}