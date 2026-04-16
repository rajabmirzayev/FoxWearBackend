package com.foxwear.orderservice.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                String userId = request.getHeader("X-User-Id");
                String username = request.getHeader("X-User-Username");
                String authHeader = request.getHeader("Authorization");

                if (userId != null) requestTemplate.header("X-User-Id", userId);
                if (username != null) requestTemplate.header("X-User-Username", username);
                if (authHeader != null) requestTemplate.header("Authorization", authHeader);
            }
        };
    }
}
