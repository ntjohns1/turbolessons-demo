package com.turbolessons.messageservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbolessons.messageservice.service.MsgCreatedEvent;
import com.turbolessons.messageservice.service.MsgCreatedEventPublisher;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.MultiValueMap;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Log4j2
@Configuration
public
class WebSocketConfig {

    @Bean
    Executor executor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    HandlerMapping handlerMapping(WebSocketHandler wsh) {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(10);
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws/messages", wsh);
        map.put("/ws/messages/{userId}", wsh);
        mapping.setUrlMap(map);
        mapping.setCorsConfigurations(Collections.singletonMap("*", new CorsConfiguration().applyPermitDefaultValues()));
        return mapping;
    }

    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }


    @Bean
    WebSocketHandler webSocketHandler(ObjectMapper objectMapper, MsgCreatedEventPublisher eventPublisher) {
        Supplier<Flux<MsgCreatedEvent>> supplier = () -> Flux.create(eventPublisher).share();
        Flux<MsgCreatedEvent> publish = Flux.defer(supplier).cache(1);
        return session -> {
            String userId = parseUserId(session.getHandshakeInfo().getUri().toString());
            log.info("WebSocket session opened for user: " + userId);

            session.receive()
                    .doOnNext(message -> {
                log.info("Received message from user " + userId + ": " + message.getPayloadAsText());
            }).doOnComplete(() -> {
                log.info("WebSocket session closed for user: " + userId);
            }).subscribe();


            Flux<WebSocketMessage> messageFlux = publish.filter(evt -> evt.getSource().getRecipient().equals(userId)).map(evt -> {
                try {
                    return objectMapper.writeValueAsString(evt.getSource());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }).map(str -> {
                log.info("sending " + str);
                return session.textMessage(str);
            });
            return session.send(messageFlux);
        };
    }


    private String parseUserId(String uri) {
        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUriString(uri).build().getQueryParams();
        return queryParams.getFirst("userId");
    }
}
