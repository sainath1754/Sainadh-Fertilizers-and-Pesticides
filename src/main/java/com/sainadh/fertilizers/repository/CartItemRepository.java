package com.sainadh.fertilizers.repository;

import com.sainadh.fertilizers.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Get all cart items for a user
    List<CartItem> findByUserId(Long userId);

    // Find specific product in user's cart
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);

    // Clear entire cart for a user
    @Transactional
    void deleteByUserId(Long userId);

    // Count items in user's cart
    long countByUserId(Long userId);
}
