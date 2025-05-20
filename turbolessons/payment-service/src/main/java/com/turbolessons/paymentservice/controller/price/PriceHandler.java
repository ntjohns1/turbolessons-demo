package com.turbolessons.paymentservice.controller.price;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface PriceHandler {
//    Mono<ServerResponse> getStandardRate(ServerRequest r);

    Mono<ServerResponse> listAll(ServerRequest r);

    Mono<ServerResponse> retrieve(ServerRequest r);

    Mono<ServerResponse> create(ServerRequest r);

    Mono<ServerResponse> update(ServerRequest r);
}
