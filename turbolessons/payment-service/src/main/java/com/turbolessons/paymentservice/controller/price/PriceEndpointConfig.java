package com.turbolessons.paymentservice.controller.price;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PriceEndpointConfig {

    private final PriceHandler handler;

    public PriceEndpointConfig(PriceHandler handler) {
        this.handler = handler;
    }

    @Bean
    RouterFunction<ServerResponse> priceRoutes() {

        return route((GET("/api/payments/price")),handler::listAll)
                .andRoute(GET("/api/payments/price/{id}"),handler::retrieve)
                .andRoute(POST("/api/payments/price"),handler::create)
                .andRoute(PUT("/api/payments/price/{id}"),handler::update);
    }
}
