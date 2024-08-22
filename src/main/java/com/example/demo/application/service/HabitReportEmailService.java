package com.example.demo.application.service;

import com.example.demo.domain.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HabitReportEmailService implements EmailService {

    private final JavaMailSender mailSender;

    public HabitReportEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String emailAddress, String subject, String report) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailAddress);
            message.setSubject(subject);
            message.setText(report);
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Failed to send email to {}: {}", emailAddress, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
