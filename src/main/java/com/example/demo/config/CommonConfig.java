package com.example.demo.config;

import com.example.demo.application.service.EmailService;
import com.example.demo.domain.model.GptClient;
import com.example.demo.domain.model.LLMClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class CommonConfig {

    @Bean
    public EmailService emailService() {
        return new EmailService(javaMailSender());
    }

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }

    @Bean
    public LLMClient llmClient() {
        return new GptClient();
    }
}
