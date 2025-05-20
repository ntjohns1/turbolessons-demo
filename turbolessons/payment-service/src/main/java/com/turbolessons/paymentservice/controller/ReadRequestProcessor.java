package com.turbolessons.paymentservice.controller;

import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ReadRequestProcessor<T> {
    Mono<T> process(ServerRequest request);
}

