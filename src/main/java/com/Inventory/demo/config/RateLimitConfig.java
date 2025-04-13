package com.Inventory.demo.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitConfig {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // Default rate: 20 requests per minute
    private Bucket createDefaultBucket() {
        Bandwidth limit = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));
        return Bucket4j.builder().addLimit(limit).build();
    }

    // Higher rate for admin users: 50 requests per minute
    private Bucket createAdminBucket() {
        Bandwidth limit = Bandwidth.classic(50, Refill.greedy(50, Duration.ofMinutes(1)));
        return Bucket4j.builder().addLimit(limit).build();
    }

    public Bucket resolveBucket(String userKey, String role) {
        return buckets.computeIfAbsent(userKey, key -> {
            if ("ADMIN".equalsIgnoreCase(role)) {
                return createAdminBucket();
            } else {
                return createDefaultBucket();
            }
        });
    }
}