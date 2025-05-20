package com.turbolessons.paymentservice.controller.meter;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface MeterHandler {

    Mono<ServerResponse> listAll(ServerRequest r);
    Mono<ServerResponse> retrieve(ServerRequest r);
    Mono<ServerResponse> create(ServerRequest r);
    Mono<ServerResponse> update(ServerRequest r);
    Mono<ServerResponse> deactivate(ServerRequest r);
    Mono<ServerResponse> reactivate(ServerRequest r);
    Mono<ServerResponse> createEvent(ServerRequest r);
    
    // Debug endpoints
    Mono<ServerResponse> getEvents(ServerRequest r);
    Mono<ServerResponse> processLessons(ServerRequest r);
    Mono<ServerResponse> ping(ServerRequest r);
}
