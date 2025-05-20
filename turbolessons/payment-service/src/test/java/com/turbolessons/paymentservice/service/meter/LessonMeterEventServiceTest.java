package com.turbolessons.paymentservice.service.meter;

import com.turbolessons.paymentservice.dto.BillingStatus;
import com.turbolessons.paymentservice.dto.LessonEvent;
import com.turbolessons.paymentservice.dto.MeterEventData;
import com.turbolessons.paymentservice.util.EventServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LessonMeterEventServiceTest {

    @Mock
    private EventServiceClient eventServiceClient;

    @Mock
    private MeterService meterService;

    @Captor
    private ArgumentCaptor<MeterEventData> meterEventDataCaptor;

    @Captor
    private ArgumentCaptor<LessonEvent> lessonEventCaptor;

    private LessonMeterEventService lessonMeterEventService;

    @BeforeEach
    void setUp() {
        lessonMeterEventService = new LessonMeterEventService(eventServiceClient, meterService);
    }

    @Test
    void processCompletedLessons_WithUnloggedCompletedLessons_ShouldCreateMeterEventsAndUpdateStatus() {
        // Given
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        LessonEvent completedLesson = createTestLesson(yesterday, BillingStatus.UNLOGGED);
        LessonEvent loggedLesson = createTestLesson(yesterday, BillingStatus.LOGGED);
        
        when(eventServiceClient.getEvents(any(LocalDate.class)))
                .thenReturn(Flux.just(completedLesson, loggedLesson));
        
        when(meterService.createMeterEvent(any()))
                .thenReturn(Mono.just(new MeterEventData("test-id", "lesson.completed", "student@test.com", "1")));
        
        when(eventServiceClient.updateEvent(any(), any()))
                .thenReturn(Mono.just(completedLesson));

        // When
        lessonMeterEventService.processCompletedLessons();

        // Then
        verify(meterService, times(1)).createMeterEvent(meterEventDataCaptor.capture());
        verify(eventServiceClient, times(1)).updateEvent(eq(completedLesson.getId()), lessonEventCaptor.capture());

        MeterEventData capturedMeterEvent = meterEventDataCaptor.getValue();
        assertThat(capturedMeterEvent.eventName()).isEqualTo("lesson.completed");
        assertThat(capturedMeterEvent.stripeCustomerId()).isEqualTo(completedLesson.getStudentEmail());
        assertThat(capturedMeterEvent.value()).isEqualTo("1");

        LessonEvent capturedLessonEvent = lessonEventCaptor.getValue();
        assertThat(capturedLessonEvent.getBillingStatus()).isEqualTo(BillingStatus.LOGGED);
    }

    @Test
    void processCompletedLessons_WithNoUnloggedLessons_ShouldNotCreateMeterEvents() {
        // Given
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        LessonEvent loggedLesson = createTestLesson(yesterday, BillingStatus.LOGGED);
        
        when(eventServiceClient.getEvents(any(LocalDate.class)))
                .thenReturn(Flux.just(loggedLesson));

        // When
        lessonMeterEventService.processCompletedLessons();

        // Then
        verify(meterService, never()).createMeterEvent(any());
        verify(eventServiceClient, never()).updateEvent(any(), any());
    }

    @Test
    void processCompletedLessons_WithFutureLesson_ShouldNotCreateMeterEvents() {
        // Given
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LessonEvent futureLesson = createTestLesson(tomorrow, BillingStatus.UNLOGGED);
        
        when(eventServiceClient.getEvents(any(LocalDate.class)))
                .thenReturn(Flux.just(futureLesson));

        // When
        lessonMeterEventService.processCompletedLessons();

        // Then
        verify(meterService, never()).createMeterEvent(any());
        verify(eventServiceClient, never()).updateEvent(any(), any());
    }

    @Test
    void processCompletedLessons_WhenMeterEventCreationFails_ShouldNotUpdateLessonStatus() {
        // Given
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        LessonEvent completedLesson = createTestLesson(yesterday, BillingStatus.UNLOGGED);
        
        when(eventServiceClient.getEvents(any(LocalDate.class)))
                .thenReturn(Flux.just(completedLesson));
        
        when(meterService.createMeterEvent(any()))
                .thenReturn(Mono.error(new RuntimeException("Failed to create meter event")));

        // When
        lessonMeterEventService.processCompletedLessons();

        // Then
        verify(meterService, times(1)).createMeterEvent(any());
        verify(eventServiceClient, never()).updateEvent(any(), any());
    }

    private LessonEvent createTestLesson(LocalDateTime startTime, BillingStatus status) {
        LessonEvent lesson = new LessonEvent(
            startTime,
            startTime.plusHours(1),
            "Test Lesson",
            "Test Student",
            "student@test.com",
            "Test Teacher",
            "teacher@test.com",
            "Test comments"
        );
        lesson.setId(1);
        lesson.setBillingStatus(status);
        return lesson;
    }
}
