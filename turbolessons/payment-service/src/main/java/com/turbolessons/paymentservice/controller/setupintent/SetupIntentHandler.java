package com.turbolessons.paymentservice.controller.setupintent;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface SetupIntentHandler {
    Mono<ServerResponse> listAll(ServerRequest r);

    //    Retrieve a SetupIntent
    Mono<ServerResponse> retrieve(ServerRequest r);

    //    Create a SetupIntent
    Mono<ServerResponse> create(ServerRequest r);

    //    Confirm a SetupIntent
    Mono<ServerResponse> confirm(ServerRequest r);

    //    Update a SetupIntent
    Mono<ServerResponse> update(ServerRequest r);

    //    Cancel a SetupIntent
    Mono<ServerResponse> cancel(ServerRequest r);
}
