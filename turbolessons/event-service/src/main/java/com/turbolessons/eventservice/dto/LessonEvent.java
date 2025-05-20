package com.turbolessons.eventservice.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "lesson_event")
public class LessonEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;

    private String student;
    private String studentEmail;
    private String teacher;
    private String teacherEmail;
    private LocalDate date;
    private String comments;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingStatus billingStatus;

    public LessonEvent() {
    }

    public LessonEvent(LocalDateTime startTime, LocalDateTime endTime, String title, String student, String studentEmail, String teacher, String teacherEmail, String comments) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.student = student;
        this.studentEmail = studentEmail;
        this.teacher = teacher;
        this.teacherEmail = teacherEmail;
        this.date = startTime.toLocalDate();
        this.comments = comments;
        this.billingStatus = BillingStatus.UNLOGGED;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        this.date = startTime.toLocalDate();
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public BillingStatus getBillingStatus() {
        return billingStatus;
    }

    public LessonEvent setBillingStatus(BillingStatus billingStatus) {
        this.billingStatus = billingStatus;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LessonEvent that = (LessonEvent) o;

        if (!Objects.equals(id,
                            that.id)) return false;
        if (!Objects.equals(startTime,
                            that.startTime)) return false;
        if (!Objects.equals(endTime,
                            that.endTime)) return false;
        if (!Objects.equals(title,
                            that.title)) return false;
        if (!Objects.equals(student,
                            that.student)) return false;
        if (!Objects.equals(studentEmail,
                            that.studentEmail)) return false;
        if (!Objects.equals(teacher,
                            that.teacher)) return false;
        if (!Objects.equals(teacherEmail,
                            that.teacherEmail)) return false;
        if (!Objects.equals(date,
                            that.date)) return false;
        if (!Objects.equals(comments,
                            that.comments)) return false;
        return billingStatus == that.billingStatus;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (student != null ? student.hashCode() : 0);
        result = 31 * result + (studentEmail != null ? studentEmail.hashCode() : 0);
        result = 31 * result + (teacher != null ? teacher.hashCode() : 0);
        result = 31 * result + (teacherEmail != null ? teacherEmail.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (billingStatus != null ? billingStatus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LessonEvent{" + "id=" + id + ", startTime=" + startTime + ", endTime=" + endTime + ", title='" + title + '\'' + ", student='" + student + '\'' + ", studentEmail='" + studentEmail + '\'' + ", teacher='" + teacher + '\'' + ", teacherEmail='" + teacherEmail + '\'' + ", date=" + date + ", comments='" + comments + '\'' + ", billingStatus=" + billingStatus + '}';
    }
}
