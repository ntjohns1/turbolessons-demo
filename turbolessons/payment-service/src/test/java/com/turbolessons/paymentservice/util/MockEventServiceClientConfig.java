package com.turbolessons.paymentservice.util;

import com.turbolessons.paymentservice.dto.LessonEvent;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@TestConfiguration
@Profile("test")
public class MockEventServiceClientConfig {

    @Bean
    @Primary
    public EventServiceClient eventServiceClient() {
        EventServiceClient mockClient = Mockito.mock(EventServiceClient.class);
        
        // Configure default behavior for the mock
        Mockito.when(mockClient.getEvents(Mockito.any(LocalDate.class)))
               .thenReturn(Flux.empty());
        
        Mockito.when(mockClient.updateEvent(Mockito.anyInt(), Mockito.any(LessonEvent.class)))
               .thenReturn(Mono.empty());
        
        return mockClient;
    }
}
