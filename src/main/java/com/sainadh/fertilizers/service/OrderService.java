package com.sainadh.fertilizers.service;

import com.sainadh.fertilizers.model.*;
import com.sainadh.fertilizers.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    // Place order — converts cart items to a new order, then clears the cart
    @Transactional
    public OrderRecord placeOrder(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            return null; // Nothing to order
        }

        // Calculate total
        double total = 0;
        for (CartItem ci : cartItems) {
            total += ci.getProduct().getPrice() * ci.getQuantity();
        }

        // Create order
        OrderRecord order = OrderRecord.builder()
                .user(user)
                .totalAmount(total)
                .status("PLACED")
                .build();

        // Add order items
        for (CartItem ci : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(ci.getProduct())
                    .quantity(ci.getQuantity())
                    .price(ci.getProduct().getPrice())
                    .build();
            order.getItems().add(orderItem);
        }

        orderRepository.save(order);

        // Clear cart after placing order
        cartItemRepository.deleteByUserId(userId);

        log.info("Order #{} placed by user {} — total: ₹{}", order.getId(), userId, total);
        return order;
    }

    // Get all orders for a user
    public List<OrderRecord> getOrdersByUser(Long userId) {
        return orderRepository.findByUserIdOrderByOrderedAtDesc(userId);
    }

    // Count orders
    public long countOrdersByUser(Long userId) {
        return orderRepository.countByUserId(userId);
    }
}
