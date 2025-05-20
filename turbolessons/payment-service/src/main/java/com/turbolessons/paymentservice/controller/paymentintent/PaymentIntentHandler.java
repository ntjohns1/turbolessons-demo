package com.turbolessons.paymentservice.controller.paymentintent;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface PaymentIntentHandler {
    Mono<ServerResponse> listAll(ServerRequest r);

    Mono<ServerResponse> retrieve(ServerRequest r);

    Mono<ServerResponse> searchByCustomer(ServerRequest r);

    Mono<ServerResponse> create(ServerRequest r);

    Mono<ServerResponse> update(ServerRequest r);

    Mono<ServerResponse> capture(ServerRequest r);

    Mono<ServerResponse> cancel(ServerRequest r);
}
