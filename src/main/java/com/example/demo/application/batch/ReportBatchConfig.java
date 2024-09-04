package com.example.demo.application.batch;

import com.example.demo.domain.service.EmailService;
import com.example.demo.domain.service.ReportProcessor;
import com.example.demo.domain.service.ReportService;
import com.example.demo.domain.service.ReportWriter;
import com.example.demo.domain.service.UserWithHabitsReader;
import com.example.demo.dto.ReportData;
import com.example.demo.infrastructure.entity.UserEntity;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class ReportBatchConfig {

    private final EntityManagerFactory entityManagerFactory;
    private final ReportService reportService;
    private final EmailService emailService;
    private final AsyncTaskExecutor taskExecutor;

    public ReportBatchConfig(EntityManagerFactory entityManagerFactory,
                             ReportService reportService,
                             EmailService emailService,
                             @Qualifier("applicationTaskExecutor") AsyncTaskExecutor taskExecutor) {
        this.entityManagerFactory = entityManagerFactory;
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
    public Step reportStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("reportStep", jobRepository)
                .<UserEntity, ReportData>chunk(10, transactionManager)
                .reader(userWithHabitsReader(null))
                .processor(reportProcessor(null))
                .writer(reportWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<UserEntity> userWithHabitsReader(@Value("#{jobParameters['time']}") Long reportTime) {
        return new UserWithHabitsReader(entityManagerFactory, reportTime);
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
