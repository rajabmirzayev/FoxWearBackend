package com.foxwear.gatewayservice.config;

import com.foxwear.gatewayservice.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final AuthenticationFilter filter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        var commonFilter = filter.apply(new AuthenticationFilter.Config());
        return builder.routes()

                .route("admin-product-service-route", r -> r
                        .path(
                                "/api/admin/products/**",
                                "/api/v1/products/**"
                        )
                        .filters(f -> f.filter(commonFilter))
                        .uri("lb://PRODUCT-SERVICE")
                )

                .route("auth-service-route", r -> r
                        .path(
                                "/api/v1/auth/**",
                                "/api/v1/users/**",
                                "/api/v1/addresses/**",
                                "/api/admin/users/**",
                                "/login/**",
                                "/oauth2/**"
                        )
                        .filters(f -> f.filter(commonFilter))
                        .uri("lb://AUTH-SERVICE")
                )

                .route("dynamic-data-service-route", r -> r
                        .path(
                                "/api/v1/dynamic/**",
                                "/api/v1/files/**",
                                "/api/admin/dynamic/**"
                        )
                        .filters(f -> f.filter(commonFilter))
                        .uri("lb://DYNAMIC-DATA-SERVICE")
                )

                .route("interaction-service-route", r -> r
                        .path(
                                "/api/v1/likes/**",
                                "/api/v1/reviews/**",
                                "/api/v1/messages/**",
                                "/api/admin/reviews/**"

                        )
                        .filters(f -> f.filter(commonFilter))
                        .uri("lb://INTERACTION-SERVICE")
                )

                .route("order-service-route", r -> r
                        .path(
                                "/api/v1/carts/**",
                                "/api/v1/orders/**",
                                "/api/v1/couriers/**",
                                "/api/admin/orders/**"
                        )
                        .filters(f -> f.filter(commonFilter))
                        .uri("lb://ORDER-SERVICE")
                )
                .build();

    }
}
