package com.sainadh.fertilizers.service;

import com.sainadh.fertilizers.model.CartItem;
import com.sainadh.fertilizers.model.Product;
import com.sainadh.fertilizers.model.User;
import com.sainadh.fertilizers.repository.CartItemRepository;
import com.sainadh.fertilizers.repository.ProductRepository;
import com.sainadh.fertilizers.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // Get all cart items for a user
    public List<CartItem> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    // Add product to cart (or increase quantity if already exists)
    @Transactional
    public void addToCart(Long userId, Long productId, int qty) {
        Optional<CartItem> existing = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (existing.isPresent()) {
            // Product already in cart — increase quantity
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + qty);
            cartItemRepository.save(item);
        } else {
            // New product — add to cart
            User user = userRepository.findById(userId).orElseThrow();
            Product product = productRepository.findById(productId).orElseThrow();

            CartItem newItem = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(qty)
                    .build();
            cartItemRepository.save(newItem);
        }
        log.info("Added product {} to cart for user {}", productId, userId);
    }

    // Remove item from cart
    @Transactional
    public void removeFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    // Update quantity of a cart item
    @Transactional
    public void updateQuantity(Long cartItemId, int qty) {
        cartItemRepository.findById(cartItemId).ifPresent(item -> {
            if (qty <= 0) {
                cartItemRepository.delete(item);
            } else {
                item.setQuantity(qty);
                cartItemRepository.save(item);
            }
        });
    }

    // Clear entire cart for a user
    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    // Calculate total price of cart
    public double getCartTotal(Long userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        double total = 0;
        for (CartItem item : items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    // Count items in cart
    public long getCartCount(Long userId) {
        return cartItemRepository.countByUserId(userId);
    }
}
