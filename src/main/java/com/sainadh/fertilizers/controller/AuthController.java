package com.sainadh.fertilizers.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error",   required = false) String error,
            @RequestParam(value = "logout",  required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired,
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
        }

        return "login";
    }
}