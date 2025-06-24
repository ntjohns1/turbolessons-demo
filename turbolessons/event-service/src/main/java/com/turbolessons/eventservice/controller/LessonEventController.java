 package com.turbolessons.eventservice.controller;

import com.turbolessons.eventservice.dto.BillingStatus;
import com.turbolessons.eventservice.dto.LessonEvent;
import com.turbolessons.eventservice.service.LessonEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//@RefreshScope
@RestController
@Tag(name = "Lesson Events", description = "API for managing lesson events")
public class LessonEventController {
    private final LessonEventService service;

    public LessonEventController(LessonEventService service) {
        this.service = service;
    }

    @Operation(summary = "Get all lesson events", description = "Retrieves all lesson events from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all lessons",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LessonEvent.class)))
    })
    @PreAuthorize("hasAnyAuthority('SCOPE_email_client', 'SCOPE_stripe_client')")
    @GetMapping("/api/lessons")
    public List<LessonEvent> getAllLessons() {

        return service.findAllLessonEvents();
    }
    @Operation(summary = "Get lesson by ID", description = "Retrieves a specific lesson event by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the lesson",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LessonEvent.class))),
        @ApiResponse(responseCode = "404", description = "Lesson not found")
    })
    @GetMapping("/api/lessons/{id}")
    public LessonEvent getLessonById(
            @Parameter(description = "ID of the lesson to retrieve") @PathVariable Integer id) {

        return service.findLessonEvent(id);
    }
    //Get Lesson Events By Teacher
    @GetMapping("/api/lessons/teacher/{teacher}")
    public List<LessonEvent> getLessonsByTeacher(@PathVariable String teacher) {

        return service.findLessonEventsByTeacher(teacher);
    }
    //Get Lesson Events By Student
    @GetMapping("/api/lessons/student/{student}")
    public List<LessonEvent> getLessonsByStudent(@PathVariable String student) {

        return service.findLessonEventsByStudent(student);
    }
    //Get Lesson Events By Date
    @PreAuthorize("hasAnyAuthority('SCOPE_email_client', 'SCOPE_stripe_client')")
    @GetMapping("/api/lessons/date/{date}")
    public List<LessonEvent> getLessonsByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.findLessonEventsByDate(date);
    }

    //Get Lesson Events By Teacher and Date
    @GetMapping("/api/lessons/{teacher}/{date}")
    public List<LessonEvent> getLessonsByTeacherAndDate(@PathVariable String teacher, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.findLessonEventsByTeacherAndDate(teacher, date);
    }
    
    //Get Lesson Events By Billing Status
    @PreAuthorize("hasAuthority('SCOPE_stripe_client')")
    @GetMapping("/api/lessons/billing/{status}")
    public List<LessonEvent> getLessonsByBillingStatus(@PathVariable BillingStatus status) {
        return service.findLessonEventsByBillingStatus(status);
    }
    
    //Get Lesson Events By Teacher and Billing Status
    @PreAuthorize("hasAuthority('SCOPE_stripe_client')")
    @GetMapping("/api/lessons/teacher/{teacher}/billing/{status}")
    public List<LessonEvent> getLessonsByTeacherAndBillingStatus(
            @PathVariable String teacher, 
            @PathVariable BillingStatus status) {
        return service.findLessonEventsByTeacherAndBillingStatus(teacher, status);
    }
    
    //Get Lesson Events By Date Range and Billing Status
    @PreAuthorize("hasAuthority('SCOPE_stripe_client')")
    @GetMapping("/api/lessons/billing/{status}/daterange")
    public List<LessonEvent> getLessonsByDateRangeAndBillingStatus(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PathVariable BillingStatus status) {
        return service.findLessonEventsByDateRangeAndBillingStatus(startDate, endDate, status);
    }
    
    //Update Lesson Event Billing Status
    @PreAuthorize("hasAuthority('SCOPE_stripe_client')")
    @PatchMapping("/api/lessons/{id}/billing/{status}")
    public void updateLessonBillingStatus(
            @PathVariable Integer id, 
            @PathVariable BillingStatus status) {
        service.updateLessonEventBillingStatus(id, status);
    }
    
    @Operation(summary = "Create a new lesson event", description = "Creates a new lesson event in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created the lesson",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LessonEvent.class))),
        @ApiResponse(responseCode = "400", description = "Invalid lesson data provided")
    })
    @PostMapping("/api/lessons")
    public LessonEvent createLesson(
            @Parameter(description = "Lesson event to create", required = true) @RequestBody LessonEvent lesson) {

        service.saveLessonEvent(lesson);
        return lesson;
    }
    //Update Lesson Event
    @PreAuthorize("hasAuthority('SCOPE_stripe_client')")
    @PutMapping("/api/lessons/{id}")
    public void updateLesson(@PathVariable Integer id, @RequestBody LessonEvent lesson) {
        LessonEvent fromService = service.findLessonEvent(id);
        fromService.setStartTime(lesson.getStartTime());
        fromService.setEndTime(lesson.getEndTime());
        fromService.setTitle(lesson.getTitle());
        fromService.setStudent(lesson.getStudent());
        fromService.setStudentEmail(lesson.getStudentEmail());
        fromService.setTeacher(lesson.getTeacher());
        fromService.setTeacherEmail(lesson.getTeacherEmail());
        fromService.setComments(lesson.getComments());
        fromService.setBillingStatus(lesson.getBillingStatus());
        service.saveLessonEvent(fromService);
    }
    //Delete Lesson Event

    @DeleteMapping("/api/lessons/{id}")
    public void deleteLesson(@PathVariable Integer id) {
        service.deleteLessonEvent(id);
    }
}
