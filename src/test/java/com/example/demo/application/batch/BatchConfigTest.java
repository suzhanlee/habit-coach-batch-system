package com.example.demo.application.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.application.service.HabitReportService;
import com.example.demo.domain.service.EmailService;
import com.example.demo.domain.service.ReportProcessor;
import com.example.demo.domain.service.ReportWriter;
import com.example.demo.domain.service.UserWithHabitsReader;
import com.example.demo.dto.ReportData;
import com.example.demo.infrastructure.entity.HabitEntity;
import com.example.demo.infrastructure.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.task.AsyncTaskExecutor;

@SpringBatchTest
@SpringBootTest(classes = BatchConfig.class)
class BatchConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    private HabitReportService reportService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private UserWithHabitsReader userWithHabitsReader;

    @Autowired
    private ReportProcessor reportProcessor;

    @Autowired
    private ReportWriter reportWriter;

    @MockBean
    @Qualifier("applicationTaskExecutor")
    private AsyncTaskExecutor taskExecutor;

    private UserEntity givenUser;
    private ReportData givenReportData;

    @BeforeEach
    void setUp() throws Exception {
        givenUser = new UserEntity("testUser", "test@example.com");
        HabitEntity habitEntity = new HabitEntity("TestHabit", "Test Description");
        habitEntity.addUserEntity(givenUser);

        givenReportData = new ReportData(1L, "testUser", "test@example.com",
                Collections.singletonList(new ReportData.HabitReportData(
                        "TestHabit", 8, 2024, 15, 2,
                        Collections.singletonList("Test Question"),
                        Collections.singletonList("Test Answer"),
                        "Test Feedback"
                ))
        );

        when(entityManagerFactory.createEntityManager()).thenReturn(mock(EntityManager.class));
        when(reportService.generateReport(any(ReportData.class))).thenReturn("monthly habit report");
        doNothing().when(emailService).sendEmail(any(), any(), any());

        when(userWithHabitsReader.read())
                .thenReturn(givenUser)
                .thenReturn(null);
    }

    @Test
    void testBatchJob() throws Exception {
        // given
        LocalDate givenDate = LocalDate.of(2024, 8, 22);
        long reportTime = givenDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", reportTime)
                .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        verifyProcessor();
        verifyWriter();
    }

    private void verifyProcessor() {
        ArgumentCaptor<ReportData> reportDataCaptor = ArgumentCaptor.forClass(ReportData.class);
        verify(reportService, times(1)).generateReport(reportDataCaptor.capture());

        ReportData capturedReportData = reportDataCaptor.getValue();
        assertEquals(givenUser.getName(), capturedReportData.getUserName());
        assertEquals(givenUser.getEmail(), capturedReportData.getEmail());
    }

    private void verifyWriter() {
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);

        verify(emailService, times(1)).sendEmail(emailCaptor.capture(), subjectCaptor.capture(),
                contentCaptor.capture());

        assertEquals("test@example.com", emailCaptor.getValue());
        assertEquals("Monthly Habit Report", subjectCaptor.getValue());
        assertEquals("monthly habit report", contentCaptor.getValue());
    }
}
