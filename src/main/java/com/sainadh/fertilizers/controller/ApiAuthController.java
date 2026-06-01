package com.sainadh.fertilizers.controller;

import com.sainadh.fertilizers.config.JwtUtil;
import com.sainadh.fertilizers.service.CustomUserDetailsService;
import com.sainadh.fertilizers.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST API Authentication Controller.
 *
 * Provides JSON-based login and register endpoints for REST API clients
 * (e.g., mobile apps, Postman testing). Returns JWT tokens on success.
 *
 * Endpoints:
 *   POST /api/auth/login    — returns JWT token
 *   POST /api/auth/register — creates account + returns JWT token
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Map<String, Object> response = new HashMap<>();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String token = jwtUtil.generateToken(userDetails);

            userDetailsService.handleLoginSuccess(username);

            response.put("success", true);
            response.put("token", token);
            response.put("username", username);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            if (username != null) {
                userDetailsService.handleLoginFailure(username);
            }
            response.put("success", false);
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String fullName = request.get("fullName");
        String email = request.get("email");
        String password = request.get("password");

        Map<String, Object> response = new HashMap<>();

        String error = userService.registerUser(username, fullName, email, password);
        if (error != null) {
            response.put("success", false);
            response.put("message", error);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Auto-login after registration
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtUtil.generateToken(userDetails);

        response.put("success", true);
        response.put("token", token);
        response.put("username", username);
        response.put("message", "Registration successful");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
