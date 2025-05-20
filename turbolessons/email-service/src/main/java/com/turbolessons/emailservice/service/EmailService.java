package com.turbolessons.emailservice.service;

import javax.mail.MessagingException;
import java.util.Map;

public interface EmailService {

    void sendHTMLMessage(String to, String subject, Map<String, Object> templateModel) throws MessagingException;

    void sendLessonReminders() throws MessagingException;
}
