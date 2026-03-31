package com.sainadh.fertilizers.init;

import com.sainadh.fertilizers.model.User;
import com.sainadh.fertilizers.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Runs once at startup — skips if admin already exists
        if (!userRepository.existsByUsername("admin")) {

            User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("Sainadh@1754"))  // BCrypt-hashed
                .email("sainadh1754@gmail.com")
                .fullName("Sainadh Admin")
                .role("ADMIN")
                .enabled(true)
                .locked(false)
                .failedAttempts(0)
                .build();

            userRepository.save(admin);

            log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            log.info("✅  Admin account created successfully");
            log.info("    Username  :  admin");
            log.info("    Email     :  sainadh1754@gmail.com");
            log.info("    Password  :  Sainadh@1754");
            log.info("    Role      :  ADMIN");
            log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        } else {
            log.info("ℹ️   Admin already exists in DB — skipping creation.");
        }
    }
}