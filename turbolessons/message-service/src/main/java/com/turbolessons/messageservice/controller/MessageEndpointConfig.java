package com.turbolessons.messageservice.controller;

import com.turbolessons.messageservice.config.CaseInsensitiveRequestPredicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
class MessageEndpointConfig {

    private final MessageHandler handler;

    MessageEndpointConfig(MessageHandler handler) {
        this.handler = handler;
    }


    @Bean
    RouterFunction<ServerResponse> routes() {
        return route(i(GET("/api/messages")), handler::all)
                .andRoute(i(GET("/api/messages/{id}")), handler::getById)
                .andRoute(i(GET("/api/messages/sender/{sender}")), handler::getBySender)
                .andRoute(i(GET("/api/messages/recipient/{recipient}")), handler::getByRecipient)
                .andRoute(i(GET("/api/messages/{sender}/to/{recipient}")), handler::getBySenderAndRecipient)
                .andRoute(i(POST("/api/messages")), handler::sendAll)
                .andRoute(i(POST("/api/messages/{id}")), handler::send)
                .andRoute(i(DELETE("/api/messages/{id}")), handler::deleteById);
    }


    private static RequestPredicate i(RequestPredicate target) {
        return new CaseInsensitiveRequestPredicate(target);
    }
}
