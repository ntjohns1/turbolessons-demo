package com.turbolessons.messageservice.controller;

import com.turbolessons.messageservice.model.Msg;
import com.turbolessons.messageservice.service.MsgService;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
class MessageHandler {


    private final MsgService messageService;

    MessageHandler(MsgService messageService) {
        this.messageService = messageService;
    }


    Mono<ServerResponse> getById(ServerRequest r) {
        return defaultReadResponse(this.messageService.get(id(r)));
    }

    Mono<ServerResponse> getBySender(ServerRequest r) {
        String sender = r.pathVariable("sender");
        Flux<Msg> messages = this.messageService.getBySender(sender);
        return defaultReadResponse(messages);
    }

    Mono<ServerResponse> getByRecipient(ServerRequest r) {
        String recipient = r.pathVariable("recipient");
        Flux<Msg> messages = this.messageService.getByRecipient(recipient);
        return defaultReadResponse(messages);
    }

    Mono<ServerResponse> getBySenderAndRecipient(ServerRequest r) {
        String sender = r.pathVariable("sender");
        String recipient = r.pathVariable("recipient");
        Flux<Msg> messages = this.messageService.getBySenderAndRecipient(sender,recipient);
        return defaultReadResponse(messages);
    }

    Mono<ServerResponse> all(ServerRequest r) {
        return defaultReadResponse(this.messageService.all());
    }

    Mono<ServerResponse> deleteById(ServerRequest r) {
        return defaultReadResponse(this.messageService.delete(id(r)));
    }

    Mono<ServerResponse> sendAll(ServerRequest request) {
        Flux<Msg> flux = request
                .bodyToFlux(Msg.class)
                .flatMap(toWrite -> this.messageService.create(toWrite.getSender(),toWrite.getRecipient(),toWrite.getMsg()));
        return defaultWriteResponse(flux);
    }

    Mono<ServerResponse> send(ServerRequest request) {
        String recipientId = id(request);
        Mono<Msg> mono = request
                .bodyToMono(Msg.class)
                .flatMap(toWrite -> this.messageService.create(toWrite.getSender(),recipientId,toWrite.getMsg()));
        return sendResponse(mono);
    }

    private static Mono<ServerResponse> defaultWriteResponse(Publisher<Msg> messages) {
        return Mono
                .from(messages)
                .flatMap(p -> ServerResponse
                        .created(URI.create("/api/messages/" + p.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .build()
                );
    }

    private static Mono<ServerResponse> sendResponse(Publisher<Msg> messages) {
        return Mono
                .from(messages)
                .flatMap(p -> ServerResponse
                        .created(URI.create("/api/messages/" + p.getRecipient()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .build()
                );
    }

    private static Mono<ServerResponse> defaultReadResponse(Publisher<Msg> messages) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(messages, Msg.class);
    }

    private static String id(ServerRequest r) {
        return r.pathVariable("id");
    }
}