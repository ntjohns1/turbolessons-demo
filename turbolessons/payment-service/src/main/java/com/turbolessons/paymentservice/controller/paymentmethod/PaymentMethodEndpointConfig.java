package com.turbolessons.paymentservice.controller.paymentmethod;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PaymentMethodEndpointConfig {

    private final PaymentMethodHandler handler;

    public PaymentMethodEndpointConfig(PaymentMethodHandler handler) {
        this.handler = handler;
    }


    @Bean
    RouterFunction<ServerResponse> paymentMethodRoutes() {
        return route((GET("/api/payments/paymentmethod/{id}")), handler::retrieve)
                .andRoute(GET("/api/payments/paymentmethod/customer/{id}"), handler::retrieveByCustomer)
                .andRoute(PUT("/api/payments/paymentmethod/attach/{id}/{customerId}"),handler::attach)
                .andRoute(PUT("/api/payments/paymentmethod/detach/{id}"),handler::detach);
    }
}