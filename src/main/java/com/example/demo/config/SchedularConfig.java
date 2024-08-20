package com.example.demo.config;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import com.example.demo.schedular.BatchJobExecutor;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedularConfig {

    @Value("${batch.cron.expression}")
    private String cronExpression;

    @Bean
    public JobDetail batchJobDetail() {
        return newJob(BatchJobExecutor.class)
                .withIdentity("batchJob", "batchGroup")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger batchJobTrigger(JobDetail batchJobDetail) {
        return newTrigger()
                .forJob(batchJobDetail)
                .withIdentity("batchTrigger", "batchGroup")
                .withSchedule(cronSchedule(cronExpression))
                .build();
    }

    @Bean
    public SchedulerFactoryBean scheduler(Trigger trigger, JobDetail job) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setJobDetails(job);
        schedulerFactory.setTriggers(trigger);
        return schedulerFactory;
    }
}
