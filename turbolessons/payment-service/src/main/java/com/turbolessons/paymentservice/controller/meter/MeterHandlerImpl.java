package com.turbolessons.paymentservice.controller.meter;

import com.turbolessons.paymentservice.controller.BaseHandler;
import com.turbolessons.paymentservice.dto.LessonEvent;
import com.turbolessons.paymentservice.dto.MeterData;
import com.turbolessons.paymentservice.dto.MeterEventData;
import com.turbolessons.paymentservice.service.meter.LessonMeterEventService;
import com.turbolessons.paymentservice.service.meter.MeterService;
import com.turbolessons.paymentservice.util.EventServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MeterHandlerImpl extends BaseHandler implements MeterHandler {

    private final MeterService meterService;
    private final EventServiceClient eventServiceClient;
    private final LessonMeterEventService lessonMeterEventService;

    public MeterHandlerImpl(MeterService meterService, 
                           EventServiceClient eventServiceClient,
                           LessonMeterEventService lessonMeterEventService) {
        this.meterService = meterService;
        this.eventServiceClient = eventServiceClient;
        this.lessonMeterEventService = lessonMeterEventService;
    }

    @Override
    public Mono<ServerResponse> listAll(ServerRequest r) {
        return handleList(r,
                          request -> meterService.listAllMeters(),
                          new ParameterizedTypeReference<>() {
                          });
    }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {
        return handleRetrieve(r,
                              request -> meterService.retrieveMeter(id(request)),
                              MeterData.class);
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(this.meterService::createMeter),
                            MeterData.class,
                            MeterData.class);
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        String id = id(r);
        return handleUpdate(r,
                            (idParam, requestBody) -> requestBody.flatMap(dto -> meterService.updateMeter(idParam,
                                                                                                          dto)),
                            id,
                            MeterData.class);
    }

    @Override
    public Mono<ServerResponse> deactivate(ServerRequest r) {
        return meterService.deactivateMeter(id(r))
                .then(ServerResponse.ok()
                              .build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    @Override
    public Mono<ServerResponse> reactivate(ServerRequest r) {
        return meterService.reactivateMeter(id(r))
                .then(ServerResponse.ok()
                              .build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    @Override
    public Mono<ServerResponse> createEvent(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(this.meterService::createMeterEvent),
                            MeterEventData.class,
                            MeterEventData.class);
    }
    
    // Debug endpoints implementation
    
    @Override
    public Mono<ServerResponse> getEvents(ServerRequest r) {
        LocalDate date = r.queryParam("date")
                .map(dateStr -> LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE))
                .orElse(LocalDate.now());
        
        log.info("Debug endpoint: Fetching events for date {}", date);
        
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(eventServiceClient.getEvents(date)
                        .doOnNext(event -> log.info("Received event: {}", event))
                        .doOnError(error -> log.error("Error fetching events", error))
                        .doOnComplete(() -> log.info("Completed fetching events")),
                      LessonEvent.class);
    }
    
    @Override
    public Mono<ServerResponse> processLessons(ServerRequest r) {
        log.info("Debug endpoint: Manually triggering processCompletedLessons");
        
        try {
            lessonMeterEventService.processCompletedLessons();
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "status", "success",
                            "message", "Process completed lessons triggered successfully"
                    ));
        } catch (Exception e) {
            log.error("Error triggering processCompletedLessons", e);
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "status", "error",
                            "message", "Error: " + e.getMessage()
                    ));
        }
    }
    
    @Override
    public Mono<ServerResponse> ping(ServerRequest r) {
        log.info("Debug endpoint: Ping received");
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "Payment service debug endpoint is working");
        response.put("timestamp", System.currentTimeMillis());
        
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }
}
