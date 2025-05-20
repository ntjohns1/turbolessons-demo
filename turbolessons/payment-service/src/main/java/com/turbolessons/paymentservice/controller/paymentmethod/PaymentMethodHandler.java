package com.turbolessons.paymentservice.controller.paymentmethod;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface PaymentMethodHandler {
    Mono<ServerResponse> retrieve(ServerRequest r);

    Mono<ServerResponse> retrieveByCustomer(ServerRequest r);

    Mono<ServerResponse> attach(ServerRequest r);

    Mono<ServerResponse> detach(ServerRequest r);
}