package com.sainadh.fertilizers.controller;

import com.sainadh.fertilizers.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;

    // Redirect root URL to landing
    @GetMapping("/")
    public String root() {
        return "redirect:/landing";
    }

    // Landing page — only reachable when logged in (enforced by SecurityConfig)
    @GetMapping("/landing")
    public String landingPage(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        userRepository.findByUsername(userDetails.getUsername()).ifPresent(user -> {
            model.addAttribute("fullName", user.getFullName());
            model.addAttribute("role",     user.getRole());
            model.addAttribute("email",    user.getEmail());
        });

        return "landing";
    }
}