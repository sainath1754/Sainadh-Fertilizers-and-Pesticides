package com.sainadh.fertilizers.controller;

import com.sainadh.fertilizers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // Show login page
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error",   required = false) String error,
            @RequestParam(value = "logout",  required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired,
            @RequestParam(value = "registered", required = false) String registered,
            Model model) {

        if ("true".equals(error)) {
            model.addAttribute("errorMsg", "Invalid username or password. Please try again.");
        } else if ("locked".equals(error)) {
            model.addAttribute("errorMsg",
                "Your account has been locked after too many failed attempts. Please contact admin.");
        } else if (logout != null) {
            model.addAttribute("successMsg", "You have been logged out successfully.");
        } else if (expired != null) {
            model.addAttribute("errorMsg", "Your session has expired. Please log in again.");
        } else if (registered != null) {
            model.addAttribute("successMsg", "Registration successful! Please log in with your new account.");
        }

        return "login";
    }

    // Show registration page
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // Handle registration form submission
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMsg", "Passwords do not match.");
            model.addAttribute("username", username);
            model.addAttribute("fullName", fullName);
            model.addAttribute("email", email);
            return "register";
        }

        // Check password length
        if (password.length() < 6) {
            model.addAttribute("errorMsg", "Password must be at least 6 characters.");
            model.addAttribute("username", username);
            model.addAttribute("fullName", fullName);
            model.addAttribute("email", email);
            return "register";
        }

        // Try to register the user
        String error = userService.registerUser(username, fullName, email, password);
        if (error != null) {
            model.addAttribute("errorMsg", error);
            model.addAttribute("username", username);
            model.addAttribute("fullName", fullName);
            model.addAttribute("email", email);
            return "register";
        }

        // Registration successful — redirect to login
        return "redirect:/login?registered=true";
    }
}