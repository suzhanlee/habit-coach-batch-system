package com.example.demo.application.batch;

import com.example.demo.domain.repository.HabitRepository;
import com.example.demo.domain.service.BadgeBatchContext;
import com.example.demo.domain.service.BadgeProcessor;
import com.example.demo.domain.service.BadgeReader;
import com.example.demo.domain.service.BadgeWriter;
import com.example.demo.domain.service.Validator;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BadgeBatchConfig {

    private final HabitRepository habitRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final Validator BadgeBatchJobValidator;
    private final BadgeBatchContext badgeReaderBatchContextManager;
    private final BadgeBatchContext badgeProcessorBatchContextManager;

    public BadgeBatchConfig(HabitRepository habitRepository, EntityManagerFactory entityManagerFactory,
                            @Qualifier("badgeBatchJobValidator") Validator badgeBatchJobValidator,
                            @Qualifier("badgeReaderBatchContextManager") BadgeBatchContext badgeReaderBatchContextManager,
                            @Qualifier("badgeProcessorBatchContextManager") BadgeBatchContext badgeProcessorBatchContextManager) {
        this.habitRepository = habitRepository;
        this.entityManagerFactory = entityManagerFactory;
        this.BadgeBatchJobValidator = badgeBatchJobValidator;
        this.badgeReaderBatchContextManager = badgeReaderBatchContextManager;
        this.badgeProcessorBatchContextManager = badgeProcessorBatchContextManager;
    }

    @Bean
    public Job badgeBatchJob(JobRepository jobRepository, @Qualifier("badgeStep") Step badgeStep) {
        return new JobBuilder("badgeBatchJob", jobRepository)
                .start(badgeStep)
                .build();
    }

    @Bean
    public Step badgeStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                          @Qualifier("badgeChunkListener") ChunkListener chunkListener) {
        return new StepBuilder("badgeStep", jobRepository)
                .chunk(10, transactionManager)
                .reader(badgeItemReader())
                .processor(badgeItemProcessor())
                .writer(badgeItemWriter())
                .listener(chunkListener)
                .build();
    }

    @Bean
    public ItemStreamReader badgeItemReader() {
        return new BadgeReader(entityManagerFactory, BadgeBatchJobValidator, badgeReaderBatchContextManager);
    }

    @Bean
    public ItemProcessor badgeItemProcessor() {
        return new BadgeProcessor(badgeProcessorBatchContextManager);
    }

    @Bean
    public ItemWriter badgeItemWriter() {
        return new BadgeWriter(habitRepository);
    }
}
