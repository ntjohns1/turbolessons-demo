package com.turbolessons.emailservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


public class Lesson implements Serializable {

    private Integer id;
    private String student;
    private String studentEmail;
    private String teacher;
    private String teacherEmail;
    private LocalDateTime date;
    private String comments;

    public Lesson() {
    }

    public Lesson(Integer id, String student, String studentEmail, String teacher, String teacherEmail, LocalDateTime date, String comments) {
        this.id = id;
        this.student = student;
        this.studentEmail = studentEmail;
        this.teacher = teacher;
        this.teacherEmail = teacherEmail;
        this.date = date;
        this.comments = comments;
    }

    public Lesson(String student, String studentEmail, String teacher, String teacherEmail, LocalDateTime date, String comments) {
        this.student = student;
        this.studentEmail = studentEmail;
        this.teacher = teacher;
        this.teacherEmail = teacherEmail;
        this.date = date;
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson that = (Lesson) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(student, that.student)) return false;
        if (!Objects.equals(studentEmail, that.studentEmail)) return false;
        if (!Objects.equals(teacher, that.teacher)) return false;
        if (!Objects.equals(teacherEmail, that.teacherEmail)) return false;
        if (!Objects.equals(date, that.date)) return false;
        return Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (student != null ? student.hashCode() : 0);
        result = 31 * result + (studentEmail != null ? studentEmail.hashCode() : 0);
        result = 31 * result + (teacher != null ? teacher.hashCode() : 0);
        result = 31 * result + (teacherEmail != null ? teacherEmail.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", student='" + student + '\'' +
                ", studentEmail='" + studentEmail + '\'' +
                ", teacher='" + teacher + '\'' +
                ", teacherEmail='" + teacherEmail + '\'' +
                ", date=" + date +
                ", comments='" + comments + '\'' +
                '}';
    }
}

