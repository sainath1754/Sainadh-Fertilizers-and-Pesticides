package com.sainadh.fertilizers.service;

import com.sainadh.fertilizers.model.Product;
import com.sainadh.fertilizers.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Product Service — handles product catalog operations.
 *
 * Redis caching is applied to frequently accessed methods:
 * - getAllProducts()    → cached under "products" (TTL: 5 min)
 * - getByCategory()    → cached under "products" by category
 * - countFertilizers() → cached under "productCount" (TTL: 10 min)
 * - countPesticides()  → cached under "productCount" (TTL: 10 min)
 *
 * This dramatically reduces database queries for product browsing.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Get all products — CACHED
    @Cacheable(value = "products", key = "'all'")
    public List<Product> getAllProducts() {
        log.debug("📦 Fetching ALL products from database (cache miss)");
        return productRepository.findAll();
    }

    // Get product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Get products by category — CACHED
    @Cacheable(value = "products", key = "#category")
    public List<Product> getByCategory(String category) {
        log.debug("📦 Fetching products for category '{}' from database (cache miss)", category);
        return productRepository.findByCategory(category);
    }

    // Count fertilizers — CACHED
    @Cacheable(value = "productCount", key = "'fertilizer'")
    public long countFertilizers() {
        log.debug("📊 Counting fertilizers from database (cache miss)");
        return productRepository.countByCategory("FERTILIZER");
    }

    // Count pesticides — CACHED
    @Cacheable(value = "productCount", key = "'pesticide'")
    public long countPesticides() {
        log.debug("📊 Counting pesticides from database (cache miss)");
        return productRepository.countByCategory("PESTICIDE");
    }

    // Clear product cache (called when products are modified)
    @CacheEvict(value = {"products", "productCount"}, allEntries = true)
    public void clearProductCache() {
        log.info("🗑️ Product cache cleared");
    }
}
