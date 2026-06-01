package com.sainadh.fertilizers.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis Cache Configuration.
 *
 * WHY REDIS?
 * - In-memory data store: serves cached data in microseconds vs milliseconds for DB
 * - Reduces database load: frequently accessed data (products, cart counts) are cached
 * - TTL support: cached data auto-expires to stay fresh
 * - Scalable: supports distributed caching across multiple app instances
 *
 * CACHE STRATEGY:
 * - "products"     → cached for 5 minutes (catalog rarely changes)
 * - "cartCount"    → cached for 1 minute (changes on add/remove)
 * - "productCount" → cached for 10 minutes (very stable data)
 *
 * NOTE: If Redis is not running, the application falls back gracefully
 * and queries the database directly (see RedisFallbackConfig).
 */
@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withCacheConfiguration("products",
                        defaultConfig.entryTtl(Duration.ofMinutes(5)))
                .withCacheConfiguration("cartCount",
                        defaultConfig.entryTtl(Duration.ofMinutes(1)))
                .withCacheConfiguration("productCount",
                        defaultConfig.entryTtl(Duration.ofMinutes(10)))
                .build();
    }
}
