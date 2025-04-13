//package com.Inventory.demo.security;
//
//import io.github.bucket4j.Bandwidth;
//import io.github.bucket4j.Bucket;
//import io.github.bucket4j.Refill;
//import org.springframework.context.annotation.Configuration;
//
//import jakarta.servlet.http.HttpServletRequest;
//import java.time.Duration;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Configuration
//public class RateLimitConfig {
//
//    // Stores one Bucket per client IP
//    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
//
//    // Called by the filter to get the Bucket for a request
//    public Bucket resolveBucket(HttpServletRequest request) {
//        String ip = request.getRemoteAddr();
//        return buckets.computeIfAbsent(ip, this::createNewBucket);
//    }
//
//    // Define the rate limit rule: 10 requests per minute
//    private Bucket createNewBucket(String ip) {
//        Refill refill = Refill.greedy(10, Duration.ofMinutes(1)); // 10 tokens per minute
//        Bandwidth limit = Bandwidth.classic(10, refill);
//        return Bucket.builder().addLimit(limit).build();
//    }
//}
