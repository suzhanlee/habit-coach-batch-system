package com.example.demo.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.mail.internet.MimeMessage;
import java.io.InputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

class EmailServiceTest {

    @Test
    @DisplayName("이메일을 정상적으로 전송한다.")
    void send_email() {
        // given
        FakeMailSender fakeMailSender = new FakeMailSender();
        EmailService emailService = new EmailService(fakeMailSender);
        String givenEmailAddress = "wlscww@kakao.com";
        String givenSubject = "월간 보고서";
        String givenReport = "monthly habit report";

        // when
        emailService.sendEmail(givenEmailAddress, givenSubject, givenReport);

        // then
        assertThat(fakeMailSender.getLastMessage()).isNotNull();
        assertThat(fakeMailSender.getLastMessage().getTo()).containsExactly(givenEmailAddress);
        assertThat(fakeMailSender.getLastMessage().getSubject()).isEqualTo(givenSubject);
        assertThat(fakeMailSender.getLastMessage().getText()).isEqualTo(givenReport);
    }

    @Test
    @DisplayName("이메일 전송에 실패한다.")
    void send_email_with_exception() {
        // given
        EmailService emailService = new EmailService(new FakeMailSender(true));
        String givenEmailAddress = "failEmail@kakao.com";
        String givenSubject = "월간 보고서";
        String givenReport = "monthly habit report";

        // when && then
        assertThatThrownBy(() -> emailService.sendEmail(givenEmailAddress, givenSubject, givenReport))
                .isInstanceOf(RuntimeException.class);
    }

    private static class FakeMailSender implements JavaMailSender {
        private SimpleMailMessage lastMessage;
        private boolean throwException;

        FakeMailSender() {
            this(false);
        }

        FakeMailSender(boolean throwException) {
            this.throwException = throwException;
        }

        @Override
        public MimeMessage createMimeMessage() {
            return null;
        }

        @Override
        public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
            return null;
        }

        @Override
        public void send(MimeMessage... mimeMessages) throws MailException {
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {
            if (throwException) {
                throw new MailException("Failed to send email") {
                };
            }
            if (simpleMessages.length > 0) {
                this.lastMessage = simpleMessages[0];
            }
        }

        public SimpleMailMessage getLastMessage() {
            return lastMessage;
        }
    }
}
