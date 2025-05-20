package com.turbolessons.eventservice.service;

import com.turbolessons.eventservice.dao.LessonEventRepo;
import com.turbolessons.eventservice.dto.BillingStatus;
import com.turbolessons.eventservice.dto.LessonEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
public class LessonEventServiceTests {

    private LessonEventRepo lessonEventRepo;
    private LessonEventService service;
    Map<Integer, LessonEvent> lessonEventStore;
    Map<String, List<LessonEvent>> lessonEventByStudentStore;
    Map<String, List<LessonEvent>> lessonEventByTeacherStore;
    Map<String, List<LessonEvent>> lessonEventByDateStore;
    Map<String, List<LessonEvent>> lessonEventByTeacherAndDateStore;
    Map<String, List<LessonEvent>> lessonEventByTeacherAndBillingStatusStore;
    Map<String, List<LessonEvent>> lessonEventByDateRangeAndBillingStatusStore;
    Map<BillingStatus, List<LessonEvent>> lessonEventByBillingStatusStore;

    LocalDateTime start1 = LocalDateTime.of(2033,
                                            Month.AUGUST,
                                            17,
                                            15,
                                            0);
    LocalDateTime end1 = start1.plusMinutes(30L);
    LocalDateTime start2 = LocalDateTime.of(2033,
                                            Month.AUGUST,
                                            17,
                                            15,
                                            30);
    LocalDateTime end2 = start2.plusMinutes(30L);
    LocalDateTime start3 = LocalDateTime.of(2033,
                                            Month.AUGUST,
                                            17,
                                            16,
                                            0);
    LocalDateTime end3 = start3.plusHours(1L);
    LocalDateTime start4 = LocalDateTime.of(2033,
                                            Month.AUGUST,
                                            18,
                                            15,
                                            30);
    LocalDateTime end4 = start4.plusMinutes(30L);

    String title1 = "StudentA - 3:00PM";
    String title2 = "StudentB - 3:00PM"; //8/18
    String title3 = "StudentC - 3:00PM";
    String title4 = "StudentD - 4:00PM";
    String student1 = "StudentA";
    String student2 = "StudentB";
    String student3 = "StudentC";
    String student4 = "StudentD";
    String teacher1 = "TeacherA";
    String teacher2 = "TeacherB";
    String studentEmail1 = "student_a@example.com";
    String studentEmail2 = "student_b@example.com";
    String studentEmail3 = "student_c@example.com";
    String studentEmail4 = "student_d@example.com";
    String teacherEmail1 = "teacher_a@example.com";
    String teacherEmail2 = "teacher_b@example.com";
    String comments = "test test";

    LessonEvent lesson;

    @BeforeEach
    public void setUp() {
        lessonEventStore = new HashMap<>();
        lessonEventByStudentStore = new HashMap<>();
        lessonEventByTeacherStore = new HashMap<>();
        lessonEventByDateStore = new HashMap<>();
        lessonEventByTeacherAndDateStore = new HashMap<>();
        lessonEventByBillingStatusStore = new HashMap<>();
        lessonEventByTeacherAndBillingStatusStore = new HashMap<>();
        lessonEventByDateRangeAndBillingStatusStore = new HashMap<>();
        
        setUpLessonEventRepoMock();
        service = new LessonEventService(lessonEventRepo);
        lesson = new LessonEvent(start1,
                                 end1,
                                 title1,
                                 student1,
                                 studentEmail1,
                                 teacher1,
                                 teacherEmail1,
                                 comments);
        lesson = new LessonEvent(start1,
                                 end1,
                                 title1,
                                 student1,
                                 studentEmail1,
                                 teacher1,
                                 teacherEmail1,
                                 comments);
        service.saveLessonEvent(lesson);
        lesson = new LessonEvent(start2,
                                 end2,
                                 title2,
                                 student2,
                                 studentEmail2,
                                 teacher1,
                                 teacherEmail1,
                                 comments);
        service.saveLessonEvent(lesson);
        lesson = new LessonEvent(start3,
                                 end3,
                                 title3,
                                 student3,
                                 studentEmail3,
                                 teacher2,
                                 teacherEmail2,
                                 comments);
        service.saveLessonEvent(lesson);
        lesson = new LessonEvent(start4,
                                 end4,
                                 title4,
                                 student4,
                                 studentEmail4,
                                 teacher1,
                                 teacherEmail1,
                                 comments);
        service.saveLessonEvent(lesson);
    }

    //Get All Lesson Event Events
    @Test
    public void testFindAllLessonEvents() {
        // Clear the store first to ensure we have a clean state
        lessonEventStore.clear();
        
        // Create and save test lessons
        LessonEvent lesson1 = new LessonEvent(start1, end1, title1, student1, studentEmail1, teacher1, teacherEmail1, comments);
        lesson1.setId(1);
        lessonEventStore.put(1, lesson1);
        
        LessonEvent lesson2 = new LessonEvent(start2, end2, title2, student2, studentEmail2, teacher1, teacherEmail1, comments);
        lesson2.setId(2);
        lessonEventStore.put(2, lesson2);
        
        LessonEvent lesson3 = new LessonEvent(start3, end3, title3, student3, studentEmail3, teacher2, teacherEmail2, comments);
        lesson3.setId(3);
        lessonEventStore.put(3, lesson3);
        
        LessonEvent lesson4 = new LessonEvent(start4, end4, title4, student4, studentEmail4, teacher1, teacherEmail1, comments);
        lesson4.setId(4);
        lessonEventStore.put(4, lesson4);

        // Reset the mock to return our new test data
        Mockito.when(lessonEventRepo.findAll())
                .thenReturn(new ArrayList<>(lessonEventStore.values()));
                
        System.out.println("****** Map Values: \n" + lessonEventStore.values());

        List<LessonEvent> allLessons = service.findAllLessonEvents();
        Assertions.assertEquals(4, allLessons.size());
        Assertions.assertEquals(start1, allLessons.get(0).getStartTime());
        Assertions.assertEquals(end1, allLessons.get(0).getEndTime());
        Assertions.assertEquals(title1, allLessons.get(0).getTitle());
        Assertions.assertEquals(student1, allLessons.get(0).getStudent());
        Assertions.assertEquals(studentEmail1, allLessons.get(0).getStudentEmail());
        Assertions.assertEquals(teacher1, allLessons.get(0).getTeacher());
        Assertions.assertEquals(teacherEmail1, allLessons.get(0).getTeacherEmail());
        Assertions.assertEquals(start1.toLocalDate(), allLessons.get(0).getDate());
        Assertions.assertEquals(comments, allLessons.get(0).getComments());
        Assertions.assertEquals(start3, allLessons.get(2).getStartTime());
        Assertions.assertEquals(end3, allLessons.get(2).getEndTime());
        Assertions.assertEquals(title3, allLessons.get(2).getTitle());
        Assertions.assertEquals(student3, allLessons.get(2).getStudent());
        Assertions.assertEquals(studentEmail3, allLessons.get(2).getStudentEmail());
        Assertions.assertEquals(teacher2, allLessons.get(2).getTeacher());
        Assertions.assertEquals(teacherEmail2, allLessons.get(2).getTeacherEmail());
        Assertions.assertEquals(start3.toLocalDate(), allLessons.get(2).getDate());
        Assertions.assertEquals(comments, allLessons.get(2).getComments());

        lessonEventStore.clear();
    }

    //Get One Lesson Event
    @Test
    public void shouldSaveFindDeleteLessonEvent() {
        LessonEvent fromService = service.findLessonEvent(lesson.getId());
        Assertions.assertEquals(lesson, fromService);
        service.deleteLessonEvent(lesson.getId());
        fromService = service.findLessonEvent(lesson.getId());
        Assertions.assertNull(fromService);
        lessonEventStore.clear();
    }

    //Get Lesson Event Events By Teacher
    @Test
    public void shouldFindLessonEventByTeacher() {

        List<LessonEvent> fromService = service.findLessonEventsByTeacher(teacher1);
        Assertions.assertEquals(3, fromService.size());
        Assertions.assertEquals(teacher1, fromService.get(0).getTeacher());
        lessonEventByTeacherStore.clear();
    }

    //Get Lesson Event Events By Student
    @Test
    public void shouldFindLessonEventByStudent() {

        List<LessonEvent> fromService = service.findLessonEventsByStudent(student1);
        Assertions.assertEquals(1, fromService.size());
        Assertions.assertEquals(student1, fromService.get(0).getStudent());
        lessonEventByStudentStore.clear();
    }

    //Get Lesson Event Events By Teacher and Date
    @Test
    public void shouldFindLessonEventByDate() {
        // Create and save 3 lessons with the same teacher and date

        // Call the method we want to test
        List<LessonEvent> fromService = service.findLessonEventsByDate(start1.toLocalDate());

        // Assert that the result is as expected
        Assertions.assertEquals(3, fromService.size());
        Assertions.assertEquals(teacher1, fromService.get(0).getTeacher());
        Assertions.assertEquals(start1.toLocalDate(), fromService.get(0).getDate());
        lessonEventByDateStore.clear();
    }


    @Test
    public void shouldFindLessonEventByTeacherAndDate() {

        // Call the method we want to test
        List<LessonEvent> fromService = service.findLessonEventsByTeacherAndDate(teacher1, start1.toLocalDate());

        // Assert that the result is as expected
        Assertions.assertEquals(2, fromService.size());
        Assertions.assertEquals(teacher1, fromService.get(0).getTeacher());
        Assertions.assertEquals(start1.toLocalDate(), fromService.get(0).getDate());
        lessonEventByTeacherAndDateStore.clear();
    }

    //Update Lesson Event
    @Test
    public void shouldUpdateLessonEvent() {
        LocalDateTime updatedTime = LocalDateTime.of(2033, Month.AUGUST, 19, 15, 0);
        LessonEvent updatedLesson = new LessonEvent(updatedTime, updatedTime.plusMinutes(30L), "updatedTitle", "updatedStudent", "updated_s@example.com", "updatedTeacher", "updated_t@example.com", "updatedComments");
        service.updateLessonEvent(lesson.getId(), updatedLesson);
        LessonEvent updatedFromService = service.findLessonEvent(lesson.getId());
        Assertions.assertEquals(updatedLesson.getStartTime(), updatedFromService.getStartTime());
        Assertions.assertEquals(updatedLesson.getEndTime(), updatedFromService.getEndTime());
        Assertions.assertEquals(updatedLesson.getTitle(), updatedFromService.getTitle());
        Assertions.assertEquals(updatedLesson.getStudent(), updatedFromService.getStudent());
        Assertions.assertEquals(updatedLesson.getStudentEmail(), updatedFromService.getStudentEmail());
        Assertions.assertEquals(updatedLesson.getTeacher(), updatedFromService.getTeacher());
        Assertions.assertEquals(updatedLesson.getTeacherEmail(), updatedFromService.getTeacherEmail());
        Assertions.assertEquals(updatedLesson.getDate(), updatedFromService.getDate());
        Assertions.assertEquals(updatedLesson.getComments(), updatedFromService.getComments());
        lessonEventStore.clear();
    }

    // Get Lesson Events By Billing Status
    @Test
    public void shouldFindLessonEventByBillingStatus() {
        // Clear stores first to ensure we have a clean state
        lessonEventStore.clear();
        lessonEventByBillingStatusStore.clear();
        
        // Create and save lessons with different billing statuses
        LessonEvent loggedLesson = new LessonEvent(start1, end1, title1, student1, studentEmail1, teacher1, teacherEmail1, comments);
        loggedLesson.setBillingStatus(BillingStatus.LOGGED);
        loggedLesson.setId(1);
        lessonEventStore.put(1, loggedLesson);
        lessonEventByBillingStatusStore.computeIfAbsent(BillingStatus.LOGGED, k -> new ArrayList<>()).add(loggedLesson);

        LessonEvent unloggedLesson = new LessonEvent(start2, end2, title2, student2, studentEmail2, teacher1, teacherEmail1, comments);
        unloggedLesson.setBillingStatus(BillingStatus.UNLOGGED);
        unloggedLesson.setId(2);
        lessonEventStore.put(2, unloggedLesson);
        lessonEventByBillingStatusStore.computeIfAbsent(BillingStatus.UNLOGGED, k -> new ArrayList<>()).add(unloggedLesson);

        // Call the method we want to test
        List<LessonEvent> loggedLessons = service.findLessonEventsByBillingStatus(BillingStatus.LOGGED);
        List<LessonEvent> unloggedLessons = service.findLessonEventsByBillingStatus(BillingStatus.UNLOGGED);

        // Assert that the result is as expected
        Assertions.assertEquals(1, loggedLessons.size());
        Assertions.assertEquals(BillingStatus.LOGGED, loggedLessons.get(0).getBillingStatus());
        Assertions.assertEquals(1, unloggedLessons.size());
        Assertions.assertEquals(BillingStatus.UNLOGGED, unloggedLessons.get(0).getBillingStatus());
    }

    // Get Lesson Events By Teacher and Billing Status
    @Test
    public void shouldFindLessonEventByTeacherAndBillingStatus() {
        // Create and save lessons with different teachers and billing statuses
        LessonEvent teacherALoggedLesson = new LessonEvent(start1, end1, title1, student1, studentEmail1, teacher1, teacherEmail1, comments);
        teacherALoggedLesson.setBillingStatus(BillingStatus.LOGGED);
        teacherALoggedLesson.setId(1);
        lessonEventStore.put(1, teacherALoggedLesson);
        
        String teacherAndStatusKey = teacher1 + "_" + BillingStatus.LOGGED.name();
        lessonEventByTeacherAndBillingStatusStore.computeIfAbsent(teacherAndStatusKey, k -> new ArrayList<>()).add(teacherALoggedLesson);

        LessonEvent teacherBLoggedLesson = new LessonEvent(start2, end2, title2, student2, studentEmail2, teacher2, teacherEmail2, comments);
        teacherBLoggedLesson.setBillingStatus(BillingStatus.LOGGED);
        teacherBLoggedLesson.setId(2);
        lessonEventStore.put(2, teacherBLoggedLesson);
        
        String teacherBAndStatusKey = teacher2 + "_" + BillingStatus.LOGGED.name();
        lessonEventByTeacherAndBillingStatusStore.computeIfAbsent(teacherBAndStatusKey, k -> new ArrayList<>()).add(teacherBLoggedLesson);

        // Call the method we want to test
        List<LessonEvent> teacherALessons = service.findLessonEventsByTeacherAndBillingStatus(teacher1, BillingStatus.LOGGED);
        List<LessonEvent> teacherBLessons = service.findLessonEventsByTeacherAndBillingStatus(teacher2, BillingStatus.LOGGED);

        // Assert that the result is as expected
        Assertions.assertEquals(1, teacherALessons.size());
        Assertions.assertEquals(teacher1, teacherALessons.get(0).getTeacher());
        Assertions.assertEquals(BillingStatus.LOGGED, teacherALessons.get(0).getBillingStatus());
        
        Assertions.assertEquals(1, teacherBLessons.size());
        Assertions.assertEquals(teacher2, teacherBLessons.get(0).getTeacher());
        Assertions.assertEquals(BillingStatus.LOGGED, teacherBLessons.get(0).getBillingStatus());
    }

    // Get Lesson Events By Date Range and Billing Status
    @Test
    public void shouldFindLessonEventByDateRangeAndBillingStatus() {
        // Create and save lessons with different dates and billing statuses
        LocalDate date1 = start1.toLocalDate();
        LocalDate date3 = date1.plusDays(2);
        
        LessonEvent day1Lesson = new LessonEvent(start1, end1, title1, student1, studentEmail1, teacher1, teacherEmail1, comments);
        day1Lesson.setBillingStatus(BillingStatus.LOGGED);
        day1Lesson.setId(1);
        lessonEventStore.put(1, day1Lesson);
        
        LessonEvent day2Lesson = new LessonEvent(start1.plusDays(1), end1.plusDays(1), title2, student2, studentEmail2, teacher1, teacherEmail1, comments);
        day2Lesson.setBillingStatus(BillingStatus.LOGGED);
        day2Lesson.setId(2);
        lessonEventStore.put(2, day2Lesson);
        
        LessonEvent day3Lesson = new LessonEvent(start1.plusDays(2), end1.plusDays(2), title3, student3, studentEmail3, teacher1, teacherEmail1, comments);
        day3Lesson.setBillingStatus(BillingStatus.UNLOGGED);
        day3Lesson.setId(3);
        lessonEventStore.put(3, day3Lesson);
        
        String dateRangeKey = date1 + "_" + date3 + "_" + BillingStatus.LOGGED.name();
        lessonEventByDateRangeAndBillingStatusStore.computeIfAbsent(dateRangeKey, k -> new ArrayList<>()).add(day1Lesson);
        lessonEventByDateRangeAndBillingStatusStore.get(dateRangeKey).add(day2Lesson);
        
        // Call the method we want to test
        List<LessonEvent> dateRangeLessons = service.findLessonEventsByDateRangeAndBillingStatus(date1, date3, BillingStatus.LOGGED);

        // Assert that the result is as expected
        Assertions.assertEquals(2, dateRangeLessons.size());
        Assertions.assertEquals(BillingStatus.LOGGED, dateRangeLessons.get(0).getBillingStatus());
        Assertions.assertEquals(BillingStatus.LOGGED, dateRangeLessons.get(1).getBillingStatus());
    }
    
    // Update Lesson Event Billing Status
    @Test
    public void shouldUpdateLessonEventBillingStatus() {
        // Create and save a lesson
        LessonEvent lesson = new LessonEvent(start1, end1, title1, student1, studentEmail1, teacher1, teacherEmail1, comments);
        lesson.setBillingStatus(BillingStatus.UNLOGGED);
        lesson.setId(1);
        lessonEventStore.put(1, lesson);
        
        // Call the method we want to test
        service.updateLessonEventBillingStatus(1, BillingStatus.LOGGED);
        
        // Get the updated lesson
        LessonEvent updatedLesson = service.findLessonEvent(1);
        
        // Assert that the billing status was updated
        Assertions.assertEquals(BillingStatus.LOGGED, updatedLesson.getBillingStatus());
    }

    private void setUpLessonEventRepoMock() {
        lessonEventRepo = mock(LessonEventRepo.class);

        Mockito.when(lessonEventRepo.findAll())
                .thenReturn(new ArrayList<>(lessonEventStore.values()));

        Mockito.when(lessonEventRepo.save(any(LessonEvent.class)))
                .then(invocation -> {
                    LessonEvent lesson = invocation.getArgument(0);
                    if (lesson.getId() == null) {
                        lesson.setId(lessonEventStore.size() + 1);
                    }
                    lessonEventStore.put(lesson.getId(), lesson);
                    lessonEventByStudentStore.computeIfAbsent(lesson.getStudent(), k -> new ArrayList<>()).add(lesson);
                    lessonEventByTeacherStore.computeIfAbsent(lesson.getTeacher(), k -> new ArrayList<>()).add(lesson);
                    String teacherAndDateKey = lesson.getTeacher() + "_" + lesson.getDate().toString();
                    lessonEventByTeacherAndDateStore.computeIfAbsent(teacherAndDateKey, k -> new ArrayList<>()).add(lesson);
                    lessonEventByDateStore.computeIfAbsent(lesson.getDate().toString(), k -> new ArrayList<>()).add(lesson);
                    
                    // Add to billing status stores
                    lessonEventByBillingStatusStore.computeIfAbsent(lesson.getBillingStatus(), k -> new ArrayList<>()).add(lesson);
                    
                    String teacherAndStatusKey = lesson.getTeacher() + "_" + lesson.getBillingStatus().name();
                    lessonEventByTeacherAndBillingStatusStore.computeIfAbsent(teacherAndStatusKey, k -> new ArrayList<>()).add(lesson);
                    
                    return lesson;
                });

        Mockito.when(lessonEventRepo.findById(any(Integer.class)))
                .then(invocation -> {
                    Integer id = invocation.getArgument(0);
                    return Optional.ofNullable(lessonEventStore.get(id));
                });

        Mockito.when(lessonEventRepo.findLessonEventByStudent(any(String.class)))
                .then(invocation -> {
                    String student = invocation.getArgument(0);
                    return Optional.ofNullable(lessonEventByStudentStore.get(student)).orElse(new ArrayList<>());
                });

        Mockito.when(lessonEventRepo.findLessonEventByTeacher(any(String.class)))
                .then(invocation -> {
                    String teacher = invocation.getArgument(0);
                    return Optional.ofNullable(lessonEventByTeacherStore.get(teacher)).orElse(new ArrayList<>());
                });

        Mockito.when(lessonEventRepo.findLessonEventByDate(any(LocalDate.class)))
                .then(invocation -> {
                    LocalDate date = invocation.getArgument(0);
                    String dateKey = date.toString();
                    return Optional.ofNullable(lessonEventByDateStore.get(dateKey)).orElse(new ArrayList<>());
                });

        Mockito.when(lessonEventRepo.findLessonEventByTeacherAndDate(any(String.class), any(LocalDate.class)))
                .then(invocation -> {
                    String teacher = invocation.getArgument(0);
                    LocalDate date = invocation.getArgument(1);
                    String teacherAndDateKey = teacher + "_" + date.toString();
                    return Optional.ofNullable(lessonEventByTeacherAndDateStore.get(teacherAndDateKey)).orElse(new ArrayList<>());
                });
                
        // Mock the new repository methods
        Mockito.when(lessonEventRepo.findLessonEventByBillingStatus(any(BillingStatus.class)))
                .then(invocation -> {
                    BillingStatus status = invocation.getArgument(0);
                    return Optional.ofNullable(lessonEventByBillingStatusStore.get(status)).orElse(new ArrayList<>());
                });
                
        Mockito.when(lessonEventRepo.findLessonEventByTeacherAndBillingStatus(any(String.class), any(BillingStatus.class)))
                .then(invocation -> {
                    String teacher = invocation.getArgument(0);
                    BillingStatus status = invocation.getArgument(1);
                    String teacherAndStatusKey = teacher + "_" + status.name();
                    return Optional.ofNullable(lessonEventByTeacherAndBillingStatusStore.get(teacherAndStatusKey)).orElse(new ArrayList<>());
                });
                
        Mockito.when(lessonEventRepo.findLessonEventByDateRangeAndBillingStatus(any(LocalDate.class), any(LocalDate.class), any(String.class)))
                .then(invocation -> {
                    LocalDate startDate = invocation.getArgument(0);
                    LocalDate endDate = invocation.getArgument(1);
                    String status = invocation.getArgument(2);
                    String dateRangeKey = startDate + "_" + endDate + "_" + status;
                    return Optional.ofNullable(lessonEventByDateRangeAndBillingStatusStore.get(dateRangeKey)).orElse(new ArrayList<>());
                });

        Mockito.doAnswer(invocation -> {
                    Integer id = invocation.getArgument(0);
                    LessonEvent lesson = lessonEventStore.remove(id);
                    if (lesson != null) {
                        lessonEventByStudentStore.get(lesson.getStudent()).remove(lesson);
                        lessonEventByTeacherStore.get(lesson.getTeacher()).remove(lesson);
                        String teacherAndDateKey = lesson.getTeacher() + "_" + lesson.getDate().toString();
                        lessonEventByTeacherAndDateStore.get(teacherAndDateKey).remove(lesson);
                        lessonEventByDateStore.get(lesson.getDate().toString()).remove(lesson);
                                
                        // Remove from billing status stores
                        if (lessonEventByBillingStatusStore.containsKey(lesson.getBillingStatus())) {
                            lessonEventByBillingStatusStore.get(lesson.getBillingStatus()).remove(lesson);
                        }
                        
                        String teacherAndStatusKey = lesson.getTeacher() + "_" + lesson.getBillingStatus().name();
                        if (lessonEventByTeacherAndBillingStatusStore.containsKey(teacherAndStatusKey)) {
                            lessonEventByTeacherAndBillingStatusStore.get(teacherAndStatusKey).remove(lesson);
                        }
                    }
                    return null;
                })
                .when(this.lessonEventRepo)
                .deleteById(any());
    }
}
