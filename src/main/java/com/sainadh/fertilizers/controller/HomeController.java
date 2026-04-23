package com.sainadh.fertilizers.controller;

import com.sainadh.fertilizers.model.User;
import com.sainadh.fertilizers.repository.UserRepository;
import com.sainadh.fertilizers.service.CartService;
import com.sainadh.fertilizers.service.OrderService;
import com.sainadh.fertilizers.service.ProductService;
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
    private final ProductService productService;
    private final CartService cartService;
    private final OrderService orderService;

    // Redirect root URL to home
    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    // Home page — only reachable when logged in (enforced by SecurityConfig)
    @GetMapping("/home")
    public String homePage(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        userRepository.findByUsername(userDetails.getUsername()).ifPresent(user -> {
            model.addAttribute("fullName", user.getFullName());
            model.addAttribute("role", user.getRole());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("userId", user.getId());

            // Stats for dashboard
            model.addAttribute("fertilizerCount", productService.countFertilizers());
            model.addAttribute("pesticideCount", productService.countPesticides());
            model.addAttribute("cartCount", cartService.getCartCount(user.getId()));
            model.addAttribute("orderCount", orderService.countOrdersByUser(user.getId()));
        });

        return "home";
    }

    // Keep /landing as an alias for backward compatibility
    @GetMapping("/landing")
    public String landingRedirect() {
        return "redirect:/home";
    }
}