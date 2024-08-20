package com.example.demo.batch;

import com.example.demo.dto.ReportData;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.EmailService;
import com.example.demo.service.ReportService;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfig {

    private final EntityManagerFactory entityManagerFactory;
    private final ReportService reportService;
    private final EmailService emailService;
    private final AsyncTaskExecutor taskExecutor;

    public BatchConfig(EntityManagerFactory entityManagerFactory,
                       ReportService reportService,
                       EmailService emailService,
                       @Qualifier("applicationTaskExecutor") AsyncTaskExecutor taskExecutor) {
        this.entityManagerFactory = entityManagerFactory;
        this.reportService = reportService;
        this.emailService = emailService;
        this.taskExecutor = taskExecutor;
    }

    @Bean
    public Job batchJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("reportJob", jobRepository)
                .start(step)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("reportStep", jobRepository)
                .<UserEntity, ReportData>chunk(10, transactionManager)
                .reader(userWithHabitsReader())
                .processor(reportProcessor(null)) // reportTime으로 런타임에 대체된다.
                .writer(reportWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<UserEntity> userWithHabitsReader() {
        return new JpaPagingItemReaderBuilder<UserEntity>()
                .name("userWithHabitsReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(
                        "SELECT DISTINCT u FROM UserEntity u LEFT JOIN FETCH u.habits h LEFT JOIN FETCH h.formationStage LEFT JOIN FETCH h.trackings")
                .pageSize(10)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<UserEntity, ReportData> reportProcessor(@Value("#{jobParameters['time']}") Long reportTime) {
        return userEntity -> reportService.createReportData(userEntity.toUser(), reportTime);
    }

    @Bean
    public ItemWriter<ReportData> reportWriter() {
        return items -> {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (ReportData reportData : items) {
                futures.add(CompletableFuture.runAsync(() -> {
                    String report = reportService.generateReport(reportData);
                    emailService.sendEmail(reportData.getEmail(), "Monthly Habit Report", report);
                }, taskExecutor));
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        };
    }
}
