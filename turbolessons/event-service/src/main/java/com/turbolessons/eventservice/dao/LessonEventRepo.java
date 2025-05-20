package com.turbolessons.eventservice.dao;

import com.turbolessons.eventservice.dto.BillingStatus;
import com.turbolessons.eventservice.dto.LessonEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonEventRepo extends JpaRepository<LessonEvent, Integer> {
    List<LessonEvent> findLessonEventByStudent(String student);
    List<LessonEvent> findLessonEventByTeacher(String teacher);
    @Query(value = "SELECT * FROM lesson_event WHERE DATE(date) = :date", nativeQuery = true)
    List<LessonEvent> findLessonEventByDate(@Param("date") LocalDate date);
    @Query(value = "SELECT * FROM lesson_event WHERE teacher = :teacher AND DATE(date) = :date", nativeQuery = true)
    List<LessonEvent> findLessonEventByTeacherAndDate(String teacher, LocalDate date);
    
    // Add method to find lesson events by billing status
    List<LessonEvent> findLessonEventByBillingStatus(BillingStatus billingStatus);
    
    // Add method to find lesson events by teacher and billing status
    List<LessonEvent> findLessonEventByTeacherAndBillingStatus(String teacher, BillingStatus billingStatus);
    
    // Add method to find lesson events by date range and billing status
    @Query(value = "SELECT * FROM lesson_event WHERE DATE(date) BETWEEN :startDate AND :endDate AND billing_status = :billingStatus", nativeQuery = true)
    List<LessonEvent> findLessonEventByDateRangeAndBillingStatus(
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate, 
            @Param("billingStatus") String billingStatus);
}
