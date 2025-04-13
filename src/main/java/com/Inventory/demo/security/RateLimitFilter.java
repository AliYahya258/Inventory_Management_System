//package com.Inventory.demo.security;
//
//import io.github.bucket4j.Bucket;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//
//@Component
//public class RateLimitFilter implements Filter {
//
//    private final RateLimitConfig rateLimitConfig;
//
//    @Autowired
//    public RateLimitFilter(RateLimitConfig rateLimitConfig) {
//        this.rateLimitConfig = rateLimitConfig;
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        Bucket bucket = rateLimitConfig.resolveBucket(httpRequest);
//
//        if (bucket.tryConsume(1)) {
//            chain.doFilter(request, response);
//        } else {
//            httpResponse.setStatus(429);
//            httpResponse.getWriter().write("Too many requests — slow down!");
//        }
//    }
//}
