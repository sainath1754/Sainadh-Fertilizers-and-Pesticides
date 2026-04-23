package com.sainadh.fertilizers.service;

import com.sainadh.fertilizers.model.User;
import com.sainadh.fertilizers.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Register a new user account
    public String registerUser(String username, String fullName, String email, String password) {

        // Check if username already taken
        if (userRepository.existsByUsername(username)) {
            return "Username is already taken. Please choose another.";
        }

        // Check if email already registered
        if (userRepository.existsByEmail(email)) {
            return "Email is already registered. Please use a different email.";
        }

        // Create and save the new user
        User newUser = User.builder()
                .username(username)
                .fullName(fullName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role("USER")
                .enabled(true)
                .locked(false)
                .failedAttempts(0)
                .build();

        userRepository.save(newUser);
        log.info("New user registered: {}", username);
        return null; // null means success
    }
}
