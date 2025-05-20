package com.turbolessons.paymentservice.util;

import com.turbolessons.paymentservice.dto.LessonEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class EventServiceClientImpl implements EventServiceClient {

    private final WebClient webClient;
    private final String eventServiceBaseUrl;

    public EventServiceClientImpl(
            WebClient.Builder webClientBuilder,
            @Value("${event-service.url:http://event-service:5001}") String eventServiceUrl,
            @Value("${event-service.endpoints.lessons:/api/lessons}") String lessonsEndpoint) {
        this.eventServiceBaseUrl = eventServiceUrl + lessonsEndpoint;
        this.webClient = webClientBuilder.build();
    }

    public Flux<LessonEvent> getEvents(LocalDate date) {
        return webClient.get()
                .uri(eventServiceBaseUrl + "/date/{date}", date.format(DateTimeFormatter.ISO_DATE))
                .retrieve()
                .bodyToFlux(LessonEvent.class);
    }

    public Mono<LessonEvent> updateEvent(Integer id, LessonEvent event) {
        return webClient.put()
                .uri(eventServiceBaseUrl + "/{id}", id)
                .bodyValue(event)
                .retrieve()
                .bodyToMono(LessonEvent.class);
    }
}
