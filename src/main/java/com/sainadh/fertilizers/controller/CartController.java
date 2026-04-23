package com.sainadh.fertilizers.controller;

import com.sainadh.fertilizers.model.OrderRecord;
import com.sainadh.fertilizers.repository.UserRepository;
import com.sainadh.fertilizers.service.CartService;
import com.sainadh.fertilizers.service.OrderService;
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
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserRepository userRepository;

    // Show cart page
    @GetMapping("/cart")
    public String cartPage(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        userRepository.findByUsername(userDetails.getUsername()).ifPresent(user -> {
            model.addAttribute("cartItems", cartService.getCartItems(user.getId()));
            model.addAttribute("cartTotal", cartService.getCartTotal(user.getId()));
            model.addAttribute("cartCount", cartService.getCartCount(user.getId()));
        });

        return "cart";
    }

    // Update quantity of a cart item
    @PostMapping("/cart/update")
    public String updateQuantity(
            @RequestParam Long cartItemId,
            @RequestParam int quantity,
            RedirectAttributes redirectAttributes) {

        cartService.updateQuantity(cartItemId, quantity);
        redirectAttributes.addFlashAttribute("successMsg", "Cart updated.");
        return "redirect:/cart";
    }

    // Remove item from cart
    @PostMapping("/cart/remove")
    public String removeItem(
            @RequestParam Long cartItemId,
            RedirectAttributes redirectAttributes) {

        cartService.removeFromCart(cartItemId);
        redirectAttributes.addFlashAttribute("successMsg", "Item removed from cart.");
        return "redirect:/cart";
    }

    // Checkout — place order
    @PostMapping("/cart/checkout")
    public String checkout(
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        userRepository.findByUsername(userDetails.getUsername()).ifPresent(user -> {
            OrderRecord order = orderService.placeOrder(user.getId());
            if (order != null) {
                redirectAttributes.addFlashAttribute("successMsg",
                    "Order #" + order.getId() + " placed successfully! Total: ₹" +
                    String.format("%.2f", order.getTotalAmount()));
            } else {
                redirectAttributes.addFlashAttribute("errorMsg", "Your cart is empty.");
            }
        });

        return "redirect:/cart";
    }
}
