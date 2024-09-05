package com.example.demo.application.batch;

import com.example.demo.domain.service.EmailService;
import com.example.demo.domain.service.ReportProcessor;
import com.example.demo.domain.service.ReportService;
import com.example.demo.domain.service.ReportWriter;
import com.example.demo.dto.ReportData;
import com.example.demo.infrastructure.entity.UserEntity;
import jakarta.persistence.EntityManagerFactory;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class ReportBatchConfig {

    private final ReportService reportService;
    private final EmailService emailService;
    private final AsyncTaskExecutor taskExecutor;

    public ReportBatchConfig(ReportService reportService,
                             EmailService emailService,
                             @Qualifier("applicationTaskExecutor") AsyncTaskExecutor taskExecutor) {
        this.reportService = reportService;
        this.emailService = emailService;
        this.taskExecutor = taskExecutor;
    }

    @Bean
    public Job reportBatchJob(JobRepository jobRepository, @Qualifier("reportStep") Step reportStep) {
        return new JobBuilder("reportBatchJob", jobRepository)
                .start(reportStep)
                .build();
    }

    @Bean
    public Step reportStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                           EntityManagerFactory entityManagerFactory) {
        return new StepBuilder("reportStep", jobRepository)
                .<UserEntity, ReportData>chunk(10, transactionManager)
                .reader(userWithHabitsReader(entityManagerFactory, null))
                .processor(reportProcessor(null))
                .writer(reportWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemStreamReader<UserEntity> userWithHabitsReader(EntityManagerFactory entityManagerFactory,
                                                             @Value("#{jobParameters['time']}") Long reportTime) {
        return new JpaPagingItemReaderBuilder<UserEntity>()
                .name("userWithHabitsReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(
                        "SELECT DISTINCT u FROM UserEntity u " +
                                "LEFT JOIN FETCH u.habits h " +
                                "LEFT JOIN FETCH h.formationStage " +
                                "LEFT JOIN h.trackings t " +
                                "WHERE t.completedDate BETWEEN :startDate AND :endDate " +
                                "OR t.completedDate IS NULL")
                .parameterValues(getParameterValues(reportTime))
                .pageSize(10)
                .build();
    }

    private Map<String, Object> getParameterValues(Long reportTime) {
        LocalDate startDate = LocalDate.ofInstant(Instant.ofEpochMilli(reportTime), ZoneId.systemDefault())
                .withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        return Map.of("startDate", startDate, "endDate", endDate);
    }


    @Bean
    @StepScope
    public ItemProcessor<UserEntity, ReportData> reportProcessor(@Value("#{jobParameters['time']}") Long reportTime) {
        return new ReportProcessor(reportService, reportTime);
    }

    @Bean
    public ItemWriter<ReportData> reportWriter() {
        return new ReportWriter(reportService, emailService, taskExecutor);
    }
}
