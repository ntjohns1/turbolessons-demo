package com.turbolessons.paymentservice.controller.paymentintent;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class PaymentIntentEndpointConfig {

    private final PaymentIntentHandler handler;

    public PaymentIntentEndpointConfig(PaymentIntentHandler handler) {
        this.handler = handler;
    }

    @Bean
    RouterFunction<ServerResponse> paymentIntentRoutes() {

      return  route((GET("/api/payments/paymentintent")), handler::listAll)
                .andRoute(GET("/api/payments/paymentintent/{id}"), handler::retrieve)
                .andRoute(GET("/api/payments/paymentintent/customer/{id}"), handler::searchByCustomer)
                .andRoute(POST("/api/payments/paymentintent"), handler::create)
                .andRoute(PUT("/api/payments/paymentintent/{id}"), handler::update)
                .andRoute(PUT("/api/payments/paymentintent/capture/{id}"), handler::capture)
                .andRoute(DELETE("/api/payments/paymentintent/{id}"), handler::cancel);
    }
}
