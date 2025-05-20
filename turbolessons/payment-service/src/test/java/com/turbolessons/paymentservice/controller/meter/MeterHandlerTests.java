package com.turbolessons.paymentservice.controller.meter;

import com.turbolessons.paymentservice.dto.BillingStatus;
import com.turbolessons.paymentservice.dto.LessonEvent;
import com.turbolessons.paymentservice.dto.MeterData;
import com.turbolessons.paymentservice.dto.MeterEventData;
import com.turbolessons.paymentservice.service.meter.LessonMeterEventService;
import com.turbolessons.paymentservice.service.meter.MeterService;
import com.turbolessons.paymentservice.util.EventServiceClient;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@Log4j2
@WebFluxTest
@Import(MeterHandlerImpl.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class MeterHandlerTests {

    @MockBean
    private MeterService meterService;
    
    @MockBean
    private EventServiceClient eventServiceClient;
    
    @MockBean
    private LessonMeterEventService lessonMeterEventService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MeterHandlerImpl meterHandler;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new MeterEndpointConfig(meterHandler).meterRoutes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    public void shouldHandleListAllMeters() {
        when(meterService.listAllMeters()).thenReturn(Mono.just(List.of(createMeterDto("mtr_123"),
                                                                        createMeterDto("mtr_234"))));

        webTestClient.get()
                .uri("/api/payments/meter")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MeterData.class)
                .hasSize(2);
    }

    @Test
    public void shouldHandleRetrieveMeter() {
        MeterData meter = createMeterDto("mtr_123");
        when(meterService.retrieveMeter(anyString())).thenReturn(Mono.just(meter));

        webTestClient.get()
                .uri("/api/payments/meter/mtr_123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MeterData.class)
                .isEqualTo(meter);
    }

    @Test
    public void shouldHandleCreateMeter() {
        MeterData meter = createMeterDto("mtr_123");
        when(meterService.createMeter(any(MeterData.class))).thenReturn(Mono.just(meter));

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/payments/meter")
                .body(Mono.just(meter),
                      MeterData.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MeterData.class)
                .isEqualTo(meter);
    }

    @Test
    public void shouldHandleUpdateMeter() {
        MeterData meter = createMeterDto("mtr_123");
        when(meterService.updateMeter(anyString(),
                                      any(MeterData.class))).thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/payments/meter/mtr_123")
                .body(Mono.just(meter),
                      MeterData.class)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    public void shouldHandleDeactivateMeter() {
        when(meterService.deactivateMeter(anyString())).thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/payments/meter/mtr_123/deactivate")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void shouldHandleReactivateMeter() {
        when(meterService.reactivateMeter(anyString())).thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/payments/meter/mtr_123/reactivate")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void shouldHandleCreateMeterEvent() {
        MeterEventData meterEvent = createMeterEventDto();
        when(meterService.createMeterEvent(any(MeterEventData.class))).thenReturn(Mono.just(meterEvent));

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/payments/meter_event")
                .body(Mono.just(meterEvent),
                      MeterEventData.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MeterEventData.class)
                .isEqualTo(meterEvent);

    }

    // Tests for debug endpoints
    @Test
    public void shouldHandlePing() {
        webTestClient.get()
                .uri("/api/payments/debug/ping")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("ok")
                .jsonPath("$.message").isEqualTo("Payment service debug endpoint is working")
                .jsonPath("$.timestamp").isNotEmpty();
    }
    
    @Test
    public void shouldHandleGetEvents() {
        // Mock the EventServiceClient response
        when(eventServiceClient.getEvents(any(LocalDate.class)))
                .thenReturn(Flux.just(createLessonEvent(1), createLessonEvent(2)));
        
        webTestClient.get()
                .uri("/api/payments/debug/events")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[1].id").isEqualTo(2);
    }
    
    @Test
    public void shouldHandleProcessLessons() {
        // No need to mock anything as we're just testing the endpoint
        webTestClient.get()
                .uri("/api/payments/debug/process-lessons")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("success")
                .jsonPath("$.message").isEqualTo("Process completed lessons triggered successfully");
    }
    
    // Helper methods to create mock MeterData and MeterEventData
    private MeterData createMeterDto(String id) {
        return new MeterData(id,
                             "Test Meter",
                             "lesson.completed");
    }

    private MeterEventData createMeterEventDto() {
        return new MeterEventData("evt_123",
                                  "lesson.completed",
                                  "cus_123",
                                  "50");
    }
    
    // Helper method to create LessonEvent objects for testing
    private LessonEvent createLessonEvent(int id) {
        LessonEvent event = new LessonEvent();
        event.setId(id);
        event.setStudentEmail("student" + id + "@example.com");
        event.setStartTime(LocalDateTime.now().minusDays(1));
        event.setEndTime(LocalDateTime.now().minusHours(1));
        event.setBillingStatus(BillingStatus.UNLOGGED);
        return event;
    }
}
