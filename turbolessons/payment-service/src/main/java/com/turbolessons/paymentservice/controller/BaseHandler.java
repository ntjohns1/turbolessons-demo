package com.turbolessons.paymentservice.controller;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public abstract class BaseHandler {


    protected <T> Mono<ServerResponse> handleList(ServerRequest request, ReadRequestProcessor<T> processor, ParameterizedTypeReference<T> typeReference) {
        return processor.process(request)
                .flatMap(responseBody -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responseBody))
                .switchIfEmpty(ServerResponse.notFound()
                                       .build())
                .onErrorResume(this::handleError);
    }


    protected <T> Mono<ServerResponse> handleRetrieve(ServerRequest request, ReadRequestProcessor<T> processor, Class<T> responseBodyClass) {
        return processor.process(request)
                .flatMap(responseBody -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responseBody))
                .switchIfEmpty(ServerResponse.notFound()
                                       .build())
                .onErrorResume(this::handleError);
    }


    protected <T, R> Mono<ServerResponse> handleCreate(ServerRequest request, CreateRequestProcessor<T, R> processor, Class<T> requestBodyClass, Class<R> responseBodyClass) {
        Mono<T> requestBody = request.bodyToMono(requestBodyClass);
        return processor.process(requestBody)
                .flatMap(responseBody -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responseBody))
                .switchIfEmpty(ServerResponse.notFound()
                                       .build())
                .onErrorResume(this::handleError);
    }

    protected <T, R> Mono<ServerResponse> handleCreateWithParam(String id, Mono<T> requestBody, CreateParamRequestProcessor<String, T, R> processor, Class<R> responseBodyClass) {
        return processor.process(id, requestBody)
                .flatMap(responseBody -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responseBody))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    protected <T> Mono<ServerResponse> handleSearch(ServerRequest request, ReadRequestProcessor<T> processor, ParameterizedTypeReference<T> typeReference) {
        return processor.process(request)
                .flatMap(responseBody -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responseBody))
                .switchIfEmpty(ServerResponse.notFound()
                                       .build())
                .onErrorResume(this::handleError);
    }


    protected <U, T> Mono<ServerResponse> handleUpdate(ServerRequest request, UpdateRequestProcessor<U, T, Void> processor, U idParam, Class<T> requestBodyClass) {
        Mono<T> requestBody = request.bodyToMono(requestBodyClass);
        return processor.process(idParam,
                                 requestBody)
                .then(ServerResponse.noContent()
                              .build())
                .onErrorResume(this::handleError);
    }

    protected <T> Mono<ServerResponse> handleCapture(ServerRequest request, ReadRequestProcessor<T> processor, Class<T> responseBodyClass) {
        return processor.process(request)
                .flatMap(responseBody -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responseBody))
                .switchIfEmpty(ServerResponse.notFound()
                                       .build())
                .onErrorResume(this::handleError);
    }

    protected <T> Mono<ServerResponse> handleDelete(ServerRequest request, ReadRequestProcessor<T> processor) {
        return processor.process(request)
                .then(ServerResponse.noContent()
                              .build())
                .onErrorResume(this::handleError);
    }


    protected String id(ServerRequest r) {
        return r.pathVariable("id");
    }

    private Mono<ServerResponse> handleError(Throwable e) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("Error message or object"));
    }
}
