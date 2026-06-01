package com.sainadh.fertilizers.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;

/**
 * Redis Fallback Configuration.
 *
 * If Redis is down or unreachable, this error handler ensures
 * the application continues to work by silently falling back
 * to the database. Cache failures are logged but never crash
 * the application.
 *
 * Implements CachingConfigurer so Spring actually picks up
 * our custom CacheErrorHandler. Without this interface,
 * the bean would be ignored.
 */
@Slf4j
@Configuration
public class RedisFallbackConfig implements CachingConfigurer {

    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {

            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                log.warn("⚠️ Redis GET failed for cache '{}', key '{}': {}", cache.getName(), key, e.getMessage());
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                log.warn("⚠️ Redis PUT failed for cache '{}', key '{}': {}", cache.getName(), key, e.getMessage());
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                log.warn("⚠️ Redis EVICT failed for cache '{}', key '{}': {}", cache.getName(), key, e.getMessage());
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                log.warn("⚠️ Redis CLEAR failed for cache '{}': {}", cache.getName(), e.getMessage());
            }
        };
    }
}
