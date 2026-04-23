package com.sainadh.fertilizers.controller;

import com.sainadh.fertilizers.repository.UserRepository;
import com.sainadh.fertilizers.service.CartService;
import com.sainadh.fertilizers.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class StoreController {

    private final ProductService productService;
    private final CartService cartService;
    private final UserRepository userRepository;

    // Show store page with all products
    @GetMapping("/store")
    public String storePage(
            @RequestParam(value = "category", required = false) String category,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        if (category != null && !category.isEmpty()) {
            model.addAttribute("products", productService.getByCategory(category));
            model.addAttribute("selectedCategory", category);
        } else {
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("selectedCategory", "ALL");
        }

        // Pass cart count for navbar badge
        userRepository.findByUsername(userDetails.getUsername()).ifPresent(user -> {
            model.addAttribute("cartCount", cartService.getCartCount(user.getId()));
        });

        return "store";
    }

    // Add product to cart
    @PostMapping("/store/add-to-cart")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        userRepository.findByUsername(userDetails.getUsername()).ifPresent(user -> {
            cartService.addToCart(user.getId(), productId, quantity);
        });

        redirectAttributes.addFlashAttribute("successMsg", "Product added to cart!");
        return "redirect:/store";
    }
}
