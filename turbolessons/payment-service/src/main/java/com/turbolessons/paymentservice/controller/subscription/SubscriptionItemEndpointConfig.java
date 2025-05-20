package com.turbolessons.paymentservice.controller.subscription;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SubscriptionItemEndpointConfig {
    
    private static final String BASE_PATH = "/api/payments/subscription_item";
    
    @Bean
    public RouterFunction<ServerResponse> subscriptionItemRoutes(SubscriptionItemHandler handler) {
        return route()
                .GET(BASE_PATH, accept(APPLICATION_JSON), handler::listAll)
                .GET(BASE_PATH + "/{id}", accept(APPLICATION_JSON), handler::retrieve)
                .POST(BASE_PATH, accept(APPLICATION_JSON), handler::create)
                .PUT(BASE_PATH + "/{id}", accept(APPLICATION_JSON), handler::update)
                .DELETE(BASE_PATH + "/{id}", accept(APPLICATION_JSON), handler::delete)
                .build();
    }
}
