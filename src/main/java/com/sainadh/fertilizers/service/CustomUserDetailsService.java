package com.sainadh.fertilizers.service;

import com.sainadh.fertilizers.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // Lock account after this many consecutive failures
    private static final int MAX_FAILED_ATTEMPTS = 5;

    private final UserRepository userRepository;

    // Called by Spring Security on every login attempt
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                log.warn("⛔ Login attempt for unknown username: {}", username);
                return new UsernameNotFoundException("User not found: " + username);
            });

        if (!user.isEnabled()) {
            throw new DisabledException("Account is disabled.");
        }

        if (user.isLocked()) {
            throw new LockedException("Account is locked. Contact admin.");
        }

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

    // Called by SecurityConfig after successful login
    public void handleLoginSuccess(String username) {
        userRepository.resetFailedAttempts(username);
        log.info("✅ Login SUCCESS for user: {}", username);
    }

    // Called by SecurityConfig after failed login
    public void handleLoginFailure(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            int newCount = user.getFailedAttempts() + 1;
            userRepository.incrementFailedAttempts(username);

            if (newCount >= MAX_FAILED_ATTEMPTS) {
                userRepository.lockAccount(username);
                log.warn("🔒 Account LOCKED: {} — {} consecutive failures", username, newCount);
            } else {
                log.warn("⚠️ Failed login {}/{} for: {}", newCount, MAX_FAILED_ATTEMPTS, username);
            }
        });
    }
}