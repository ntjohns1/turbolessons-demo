package com.turbolessons.paymentservice.util;

import com.turbolessons.paymentservice.dto.LessonEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface EventServiceClient {
    Flux<LessonEvent> getEvents(LocalDate date);
    Mono<LessonEvent> updateEvent(Integer id, LessonEvent event);
}
