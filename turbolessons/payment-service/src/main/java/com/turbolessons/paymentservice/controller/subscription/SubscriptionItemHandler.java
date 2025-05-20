package com.turbolessons.paymentservice.controller.subscription;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface SubscriptionItemHandler {
    
    /**
     * List all subscription items for a subscription
     */
    Mono<ServerResponse> listAll(ServerRequest r);
    
    /**
     * Retrieve a subscription item by ID
     */
    Mono<ServerResponse> retrieve(ServerRequest r);
    
    /**
     * Create a new subscription item
     */
    Mono<ServerResponse> create(ServerRequest r);
    
    /**
     * Update an existing subscription item
     */
    Mono<ServerResponse> update(ServerRequest r);
    
    /**
     * Delete a subscription item
     */
    Mono<ServerResponse> delete(ServerRequest r);
}
