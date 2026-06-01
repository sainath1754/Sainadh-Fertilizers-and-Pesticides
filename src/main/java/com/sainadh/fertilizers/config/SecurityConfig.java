package com.sainadh.fertilizers.config;

import com.sainadh.fertilizers.service.CustomUserDetailsService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtAuthFilter jwtAuthFilter;

    // ── BCrypt Password Encoder (strength 12) ─────────────────────
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    // ── Wires UserDetailsService + PasswordEncoder together ────────
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ── Main Security Rules ────────────────────────────────────────
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF protection is ON — Thymeleaf injects the token automatically
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                .requestMatchers("/login", "/register", "/error").permitAll()
                .requestMatchers("/api/auth/**").permitAll()     // REST API auth endpoints
                .anyRequest().authenticated()          // All other pages need login
            )
            .formLogin(form -> form
                .loginPage("/login")                   // Our custom login page
                .loginProcessingUrl("/login")          // Spring Security handles POST
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailureHandler())
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)           // Destroy session on logout
                .deleteCookies("JSESSIONID", "jwt")    // Remove session + JWT cookies
                .clearAuthentication(true)
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)                    // Max 1 active session per user
                .expiredUrl("/login?expired=true")
            )
            .authenticationProvider(authenticationProvider())
            // Add JWT filter before the default authentication filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ── Success: reset failed attempts + issue JWT cookie ──────────
    private AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> {
            userDetailsService.handleLoginSuccess(authentication.getName());

            // Generate JWT and set as HttpOnly cookie
            var userDetails = (org.springframework.security.core.userdetails.UserDetails)
                    authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);       // Prevents XSS access
            jwtCookie.setSecure(false);        // Set to true in production (HTTPS)
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60); // 24 hours
            response.addCookie(jwtCookie);

            response.sendRedirect("/home");
        };
    }

    // ── Failure: increment counter, lock if threshold reached ──────
    private AuthenticationFailureHandler loginFailureHandler() {
        return (request, response, exception) -> {
            String username = request.getParameter("username");
            if (username != null && !username.isBlank()) {
                userDetailsService.handleLoginFailure(username);
            }
            // Send to /login?error=locked or /login?error=true
            String param = (exception instanceof LockedException) ? "locked" : "true";
            response.sendRedirect("/login?error=" + param);
        };
    }
}