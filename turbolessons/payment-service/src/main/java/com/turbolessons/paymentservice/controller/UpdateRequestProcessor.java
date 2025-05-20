package com.turbolessons.paymentservice.controller;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface UpdateRequestProcessor<U, T, V> {
    Mono<Void> process(U idParam, Mono<T> requestBody);
}


