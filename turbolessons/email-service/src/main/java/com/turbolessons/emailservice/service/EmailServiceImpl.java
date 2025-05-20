package com.turbolessons.emailservice.service;

//import dto.com.turbolessons.emailservice.Lesson;
//import util.com.turbolessons.emailservice.EventServiceClient;
import com.turbolessons.emailservice.dto.Lesson;
import com.turbolessons.emailservice.util.EventServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import reactor.core.publisher.Flux;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service("email-service")
public class EmailServiceImpl implements EmailService {

    private static final String NOREPLY_ADDRESS = "nelsontjohns@gmail.com";

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    @Autowired
    private EventServiceClient eventServiceClient;

    @Value("classpath:/mail-logo.png")
    private Resource resourceFile;

    @Override
    public void sendHTMLMessage(String to, String subject, Map<String, Object> templateModel) throws MessagingException {

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("template-thymeleaf.html", thymeleafContext);
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(NOREPLY_ADDRESS);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.addInline("attachment.png", resourceFile);
        emailSender.send(message);
    }

    @Scheduled(cron = "0 10 20 * * *")
    public void sendLessonReminders() throws MessagingException {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Flux<Lesson> lessons = eventServiceClient.getEvents(tomorrow);
        for (Lesson l : lessons.toIterable()) {
            System.out.println(l);
            int hour = l.getDate().getHour();
            int minute = l.getDate().getMinute();
            String am_pm = (hour >= 12) ? "PM" : "AM";
            if (hour > 12) {
                hour -= 12;
            } else if (hour == 0) {
                hour = 12;
            }
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("recipientName", l.getStudent());
            String formattedTime = String.format("%02d:%02d %s", hour, minute, am_pm); // makes sure it's always 2 digits
            templateModel.put("text", String.format("This is an auto reminder from TURBOLESSONS. You have a lesson scheduled for tomorrow at %s.", formattedTime));
            templateModel.put("senderName", "TURBOLESSONS");
            sendHTMLMessage(l.getStudentEmail(), "Lesson Reminder", templateModel);
        }
        System.out.println("Got Lessons for: " + tomorrow);
    }

}
