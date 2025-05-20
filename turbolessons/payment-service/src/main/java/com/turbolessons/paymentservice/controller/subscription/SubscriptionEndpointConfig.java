package com.turbolessons.paymentservice.controller.subscription;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SubscriptionEndpointConfig {
    private final SubscriptionHandler handler;

    public SubscriptionEndpointConfig(SubscriptionHandler handler) {
        this.handler = handler;
    }

    @Bean
    RouterFunction<ServerResponse> subscriptionRoutes() {

        return route((GET("/api/payments/subscription")), handler::listAll)
                .andRoute(GET("/api/payments/subscription/{id}"), handler::retrieve)
                .andRoute(POST("/api/payments/subscription"), handler::create)
                .andRoute(PUT("/api/payments/subscription/{id}"), handler::update)
                .andRoute(DELETE("/api/payments/subscription/{id}"), handler::cancel);
    }
}
