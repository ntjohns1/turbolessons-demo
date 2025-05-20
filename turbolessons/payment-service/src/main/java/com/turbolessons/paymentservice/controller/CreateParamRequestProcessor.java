package com.turbolessons.paymentservice.controller;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface CreateParamRequestProcessor<I, T, R> {
    Mono<R> process(I id, Mono<T> requestBody);
}
