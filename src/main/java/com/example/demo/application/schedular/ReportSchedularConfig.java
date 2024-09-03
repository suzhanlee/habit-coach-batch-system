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
public class ReportSchedularConfig {

    @Value("${batch.report.cron.expression}")
    private String cronExpression;

    @Bean
    public JobDetail reportBatchJobDetail() {
        return newJob(ReportBatchJobExecutor.class)
                .withIdentity("reportBatchJob", "reportBatchGroup")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger reportBatchJobTrigger(@Qualifier("reportBatchJobDetail") JobDetail reportBatchJobDetail) {
        return newTrigger()
                .forJob(reportBatchJobDetail)
                .withIdentity("reportBatchTrigger", "reportBatchGroup")
                .withSchedule(cronSchedule(cronExpression))
                .build();
    }

    @Bean
    public SchedulerFactoryBean reportScheduler(@Qualifier("reportBatchJobTrigger") Trigger reportBatchJobTrigger,
                                                @Qualifier("reportBatchJobDetail") JobDetail reportBatchJobDetail) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setJobDetails(reportBatchJobDetail);
        schedulerFactory.setTriggers(reportBatchJobTrigger);
        return schedulerFactory;
    }
}
