package com.turbolessons.paymentservice.controller.customer;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface CustomerHandler {
    Mono<ServerResponse> listAll(ServerRequest r);

    Mono<ServerResponse> retrieve(ServerRequest r);

    Mono<ServerResponse> search(ServerRequest r);

    Mono<ServerResponse> create(ServerRequest r);

    Mono<ServerResponse> update(ServerRequest r);

    Mono<ServerResponse> delete(ServerRequest r);
}
