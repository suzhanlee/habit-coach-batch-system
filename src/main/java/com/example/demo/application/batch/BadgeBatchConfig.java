package com.example.demo.application.batch;

import com.example.demo.domain.repository.HabitRepository;
import com.example.demo.domain.service.BadgeProcessor;
import com.example.demo.domain.service.BadgeWriter;
import com.example.demo.infrastructure.entity.HabitEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BadgeBatchConfig {

    private final HabitRepository habitRepository;
    private final EntityManagerFactory entityManagerFactory;

    public BadgeBatchConfig(HabitRepository habitRepository, EntityManagerFactory entityManagerFactory) {
        this.habitRepository = habitRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public Job badgeBatchJob(JobRepository jobRepository, @Qualifier("badgeStep") Step badgeStep) {
        return new JobBuilder("badgeBatchJob", jobRepository)
                .start(badgeStep)
                .build();
    }

    @Bean
    public Step badgeStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("badgeStep", jobRepository)
                .chunk(10, transactionManager)
                .reader(badgeItemReader())
                .processor(badgeItemProcessor())
                .writer(badgeItemWriter())
                .build();
    }

    @Bean
    public ItemStreamReader<HabitEntity> badgeItemReader() {
        return new JpaPagingItemReaderBuilder<HabitEntity>()
                .name("badgeReader")
                .queryString("SELECT h FROM HabitEntity h")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(10)
                .build();
    }

    @Bean
    public ItemProcessor badgeItemProcessor() {
        return new BadgeProcessor();
    }

    @Bean
    public ItemWriter badgeItemWriter() {
        return new BadgeWriter(habitRepository);
    }
}
