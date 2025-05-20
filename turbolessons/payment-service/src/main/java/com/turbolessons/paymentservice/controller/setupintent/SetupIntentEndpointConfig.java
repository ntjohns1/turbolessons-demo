package com.turbolessons.paymentservice.controller.setupintent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SetupIntentEndpointConfig {

    private final SetupIntentHandler handler;

    public SetupIntentEndpointConfig(SetupIntentHandler handler) {
        this.handler = handler;
    }

    @Bean
    RouterFunction<ServerResponse> setupIntentRoutes() {

        return route((GET("/api/payments/setupintent")),handler::listAll)
                .andRoute(GET("/api/payments/setupintent/{id}"),handler::retrieve)
                .andRoute(POST("/api/payments/setupintent"),handler::create)
                .andRoute(PUT("/api/payments/setupintent/confirm/{id}"),handler::confirm)
                .andRoute(PUT("/api/payments/setupintent/{id}"),handler::update)
                .andRoute(DELETE("/api/payments/setupintent/{id}"),handler::cancel);
    }
}
