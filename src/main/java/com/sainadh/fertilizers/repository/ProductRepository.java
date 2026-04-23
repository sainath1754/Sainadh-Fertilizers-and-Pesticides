package com.sainadh.fertilizers.repository;

import com.sainadh.fertilizers.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find all products by category (FERTILIZER or PESTICIDE)
    List<Product> findByCategory(String category);

    // Count products by category
    long countByCategory(String category);
}
