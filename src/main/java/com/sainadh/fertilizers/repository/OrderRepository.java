package com.sainadh.fertilizers.repository;

import com.sainadh.fertilizers.model.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderRecord, Long> {

    // Get all orders for a user, most recent first
    List<OrderRecord> findByUserIdOrderByOrderedAtDesc(Long userId);

    // Count orders for a user
    long countByUserId(Long userId);
}
