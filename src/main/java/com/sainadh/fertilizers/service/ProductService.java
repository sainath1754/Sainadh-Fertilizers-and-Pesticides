package com.sainadh.fertilizers.service;

import com.sainadh.fertilizers.model.Product;
import com.sainadh.fertilizers.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Get products by category
    public List<Product> getByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    // Count fertilizers
    public long countFertilizers() {
        return productRepository.countByCategory("FERTILIZER");
    }

    // Count pesticides
    public long countPesticides() {
        return productRepository.countByCategory("PESTICIDE");
    }
}
