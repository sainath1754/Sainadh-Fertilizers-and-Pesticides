package com.sainadh.fertilizers.repository;

import com.sainadh.fertilizers.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
