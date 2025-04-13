package com.Inventory.demo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;


@Configuration
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis", matchIfMissing = false)
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))  // Set time-to-live to 10 minutes
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer())
                );

//        // Custom cache configurations
//        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
//
//        // Product cache: longer TTL as they change less frequently
//        cacheConfigurations.put("products", cacheConfiguration.entryTtl(Duration.ofMinutes(30)));
//
//        // Stock cache: shorter TTL as they change frequently
//        cacheConfigurations.put("stocks", cacheConfiguration.entryTtl(Duration.ofMinutes(5)));
//
//        // Store cache: medium TTL
//        cacheConfigurations.put("stores", cacheConfiguration.entryTtl(Duration.ofMinutes(20)));
//
//        // Audit logs: short TTL
//        cacheConfigurations.put("auditLogs", cacheConfiguration.entryTtl(Duration.ofMinutes(2)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration)
                .withCacheConfiguration("products",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30)))
                .withCacheConfiguration("stocks",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)))
                .build();
    }
}