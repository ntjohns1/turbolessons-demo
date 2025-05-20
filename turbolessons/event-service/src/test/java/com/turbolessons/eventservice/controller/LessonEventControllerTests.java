package com.turbolessons.eventservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbolessons.eventservice.dto.BillingStatus;
import com.turbolessons.eventservice.dto.LessonEvent;
import com.turbolessons.eventservice.service.LessonEventService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonEventController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LessonEventControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LessonEventService service;

    private LessonEvent lesson1;
    private LessonEvent lesson2;
    private LessonEvent loggedLesson;
    private LessonEvent invoicedLesson;

    @BeforeEach
    void setUp() {
        LocalDateTime start1 = LocalDateTime.of(2033, Month.AUGUST, 17, 15, 0);
        LocalDateTime end1 = start1.plusMinutes(30);
        lesson1 = new LessonEvent(start1, end1, "StudentA - 3:00PM", "StudentA", "student_a@example.com", "TeacherA", "teacher_a@example.com", "comments");
        lesson1.setId(1);
        
        LocalDateTime start2 = LocalDateTime.of(2033, Month.AUGUST, 18, 15, 0);
        LocalDateTime end2 = start2.plusMinutes(30);
        lesson2 = new LessonEvent(start2, end2, "StudentB - 3:00PM", "StudentB", "student_b@example.com", "TeacherB", "teacher_b@example.com", "comments");
        lesson2.setId(2);
        
        // Create lessons with specific billing statuses for testing
        loggedLesson = new LessonEvent(start1, end1, "Logged Lesson", "StudentC", "student_c@example.com", "TeacherA", "teacher_a@example.com", "logged status");
        loggedLesson.setId(3);
        loggedLesson.setBillingStatus(BillingStatus.LOGGED);
        
        invoicedLesson = new LessonEvent(start2, end2, "Invoiced Lesson", "StudentD", "student_d@example.com", "TeacherA", "teacher_a@example.com", "invoiced status");
        invoicedLesson.setId(4);
        invoicedLesson.setBillingStatus(BillingStatus.INVOICED);
    }

    @Test
    void getAllLessons_ShouldReturnLessonsList() throws Exception {
        when(service.findAllLessonEvents()).thenReturn(Arrays.asList(lesson1, lesson2));

        mockMvc.perform(get("/api/lessons"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(lesson1.getId()))
            .andExpect(jsonPath("$[1].id").value(lesson2.getId()));
    }

    @Test
    void createLesson_ShouldReturnCreatedLesson() throws Exception {
        when(service.saveLessonEvent(any(LessonEvent.class))).thenReturn(lesson1);

        mockMvc.perform(post("/api/lessons")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(lesson1)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(lesson1.getId()))
            .andExpect(jsonPath("$.student").value(lesson1.getStudent()));
    }

    @Test
    void getLessonById_ShouldReturnLesson() throws Exception {
        when(service.findLessonEvent(eq(1))).thenReturn(lesson1);

        mockMvc.perform(get("/api/lessons/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lesson1.getId()))
            .andExpect(jsonPath("$.teacher").value(lesson1.getTeacher()));
    }

    @Test
    void updateLesson_ShouldUpdateExistingLesson() throws Exception {
        when(service.findLessonEvent(eq(1))).thenReturn(lesson1);
        when(service.saveLessonEvent(any(LessonEvent.class))).thenReturn(lesson1);

        lesson1.setComments("Updated comments");
        mockMvc.perform(put("/api/lessons/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(lesson1)))
            .andExpect(status().isOk());
    }

    @Test
    void deleteLesson_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/lessons/{id}", 1))
            .andExpect(status().isOk());
    }
    
    @Test
    void getLessonsByBillingStatus_ShouldReturnFilteredLessons() throws Exception {
        List<LessonEvent> loggedLessons = Arrays.asList(loggedLesson);
        when(service.findLessonEventsByBillingStatus(BillingStatus.LOGGED)).thenReturn(loggedLessons);

        mockMvc.perform(get("/api/lessons/billing/{status}", "LOGGED"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(loggedLesson.getId()))
            .andExpect(jsonPath("$[0].billingStatus").value(BillingStatus.LOGGED.name()));
    }
    
    @Test
    void getLessonsByTeacherAndBillingStatus_ShouldReturnFilteredLessons() throws Exception {
        List<LessonEvent> teacherLoggedLessons = Arrays.asList(loggedLesson);
        when(service.findLessonEventsByTeacherAndBillingStatus("TeacherA", BillingStatus.LOGGED)).thenReturn(teacherLoggedLessons);

        mockMvc.perform(get("/api/lessons/teacher/{teacher}/billing/{status}", "TeacherA", "LOGGED"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(loggedLesson.getId()))
            .andExpect(jsonPath("$[0].teacher").value("TeacherA"))
            .andExpect(jsonPath("$[0].billingStatus").value(BillingStatus.LOGGED.name()));
    }
    
    @Test
    void getLessonsByDateRangeAndBillingStatus_ShouldReturnFilteredLessons() throws Exception {
        LocalDate startDate = LocalDate.of(2033, Month.AUGUST, 15);
        LocalDate endDate = LocalDate.of(2033, Month.AUGUST, 20);
        List<LessonEvent> dateRangeLessons = Arrays.asList(loggedLesson);
        
        when(service.findLessonEventsByDateRangeAndBillingStatus(eq(startDate), eq(endDate), eq(BillingStatus.LOGGED)))
            .thenReturn(dateRangeLessons);

        mockMvc.perform(get("/api/lessons/billing/{status}/daterange", "LOGGED")
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(loggedLesson.getId()))
            .andExpect(jsonPath("$[0].billingStatus").value(BillingStatus.LOGGED.name()));
    }
    
    @Test
    void updateLessonBillingStatus_ShouldUpdateStatus() throws Exception {
        // Test the PATCH endpoint for updating just the billing status
        mockMvc.perform(patch("/api/lessons/{id}/billing/{status}", 1, "INVOICED"))
            .andExpect(status().isOk());
            
        // Verify the service method was called with correct parameters
        verify(service).updateLessonEventBillingStatus(1, BillingStatus.INVOICED);
    }
}
