package com.example.demo.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.domain.model.User;
import com.example.demo.dto.ReportData;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.Chunk;
import org.springframework.core.task.AsyncTaskExecutor;

class ReportWriterTest {

    @Test
    @DisplayName("ReportData로 월간 보고서를 생성한 후 이메일을 전송한다")
    void write() {
        // given
        FakeReportService reportService = new FakeReportService();
        FakeEmailService emailService = new FakeEmailService();
        FakeAsyncExecutor taskExecutor = new FakeAsyncExecutor();
        ReportWriter reportWriter = new ReportWriter(reportService, emailService, taskExecutor);

        List<ReportData> givenReportDatas = createGivenReportDatas();
        Chunk<ReportData> givenChunk = new Chunk<>(givenReportDatas);

        // when
        reportWriter.write(givenChunk);

        // then
        assertEquals(2, reportService.getGenerateReportCount());
        assertEquals(2, emailService.getSentEmailCount());
        assertEquals(2, taskExecutor.getExecutedTaskCount());

        assertTrue(emailService.isEmailSent("suchan1@example.com", "Monthly Habit Report", "Report for suchan1"));
        assertTrue(emailService.isEmailSent("suchan2@example.com", "Monthly Habit Report", "Report for suchan2"));
    }

    private List<ReportData> createGivenReportDatas() {
        List<ReportData> reportDataList = new ArrayList<>();
        reportDataList.add(new ReportData("suchan1", "suchan1@example.com"));
        reportDataList.add(new ReportData("suchan2", "suchan2@example.com"));
        return reportDataList;
    }

    private static class FakeReportService implements ReportService {
        private int generateReportCount = 0;

        @Override
        public ReportData createReportData(User user, Long reportTime) {
            return new ReportData();
        }

        @Override
        public String generateReport(ReportData reportData) {
            this.generateReportCount++;
            return "Report for " + reportData.getUserName();
        }

        public int getGenerateReportCount() {
            return generateReportCount;
        }
    }

    private static class FakeEmailService implements EmailService {
        private int sentEmailCount = 0;
        private final List<EmailRecord> sentEmails = new ArrayList<>();

        @Override
        public void sendEmail(String emailAddress, String subject, String report) {
            this.sentEmailCount++;
            sentEmails.add(new EmailRecord(emailAddress, subject, report));
        }

        public int getSentEmailCount() {
            return this.sentEmailCount;
        }

        public boolean isEmailSent(String emailAddress, String subject, String report) {
            return sentEmails.stream()
                    .anyMatch(email -> email.emailAddress.equals(emailAddress) &&
                            email.subject.equals(subject) &&
                            email.report.equals(report));
        }

        private record EmailRecord(String emailAddress, String subject, String report) {
        }
    }

    private static class FakeAsyncExecutor implements AsyncTaskExecutor {
        private int executedTaskCount = 0;

        @Override
        public void execute(Runnable task) {
            this.executedTaskCount++;
            task.run();
        }

        public int getExecutedTaskCount() {
            return executedTaskCount;
        }
    }
}
