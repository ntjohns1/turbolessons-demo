package com.turbolessons.paymentservice.controller.invoice;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface InvoiceHandler {
    Mono<ServerResponse> listAll(ServerRequest r);

    Mono<ServerResponse> listAllByCustomer(ServerRequest r);


    Mono<ServerResponse> listAllBySubscription(ServerRequest r);

    Mono<ServerResponse> retrieve(ServerRequest r);

    Mono<ServerResponse> retrieveUpcoming(ServerRequest r);

    Mono<ServerResponse> create(ServerRequest r);

    Mono<ServerResponse> update(ServerRequest r);

    Mono<ServerResponse> deleteDraft(ServerRequest r);

    Mono<ServerResponse> finalize(ServerRequest r);

    Mono<ServerResponse> payInvoice(ServerRequest r);

    Mono<ServerResponse> voidInvoice(ServerRequest r);

    Mono<ServerResponse> markUncollectible(ServerRequest r);

    Mono<ServerResponse> retrieveLineItems(ServerRequest r);

    Mono<ServerResponse> retrieveUpcomingLineItems(ServerRequest r);


}
