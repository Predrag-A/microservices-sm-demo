package com.predrag.a.api.gateway.config;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator gatewayRoutes(final RouteLocatorBuilder builder) {
        return builder.routes()
                .route(this::authService)
                .route(this::mediaService)
                .route(this::postService)
                .route(this::graphService)
                .route(this::feedService)
                .build();
    }

    private Buildable<Route> authService(final PredicateSpec spec) {
        return spec.path("/auth/**")
                .or()
                .path("/users/**")
                .uri("lb://auth-service");
    }

    private Buildable<Route> mediaService(final PredicateSpec spec) {
        return spec.path("/media/**")
                .uri("lb://media-service");
    }

    private Buildable<Route> postService(final PredicateSpec spec) {
        return spec.path("/posts/**")
                .uri("lb://post-service");
    }

    private Buildable<Route> graphService(final PredicateSpec spec) {
        return spec.path("/graph/**")
                .uri("lb://graph-service");
    }

    private Buildable<Route> feedService(final PredicateSpec spec) {
        return spec.path("/feed/**")
                .uri("lb://feed-service");
    }
}
