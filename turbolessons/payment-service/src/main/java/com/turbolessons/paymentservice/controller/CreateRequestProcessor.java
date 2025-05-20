package com.turbolessons.paymentservice.controller;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface CreateRequestProcessor<T, R> {
    Mono<R> process(Mono<T> requestBody);
}

