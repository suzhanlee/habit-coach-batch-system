package com.example.demo.application.schedular;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class BadgeScheduleConfig {

    @Value("${batch.badge.cron.expression}")
    private String cronExpression;

    @Bean
    public JobDetail badgeBatchJobDetail() {
        return newJob(BadgeBatchJobExecutor.class)
                .withIdentity("badgeBatchJob", "badgeBatchGroup")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger badgeBatchJobTrigger(@Qualifier("badgeBatchJobDetail") JobDetail badgeBatchJobDetail) {
        return newTrigger()
                .forJob(badgeBatchJobDetail)
                .withIdentity("badgeBatchTrigger", "badgeBatchGroup")
                .withSchedule(cronSchedule(cronExpression))
                .build();
    }

    @Bean
    public SchedulerFactoryBean badgeScheduler(@Qualifier("badgeBatchJobTrigger") Trigger badgeBatchJobTrigger, @Qualifier("badgeBatchJobDetail") JobDetail badgeBatchJobDetail) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setJobDetails(badgeBatchJobDetail);
        schedulerFactory.setTriggers(badgeBatchJobTrigger);
        return schedulerFactory;
    }
}
