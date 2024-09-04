package com.example.demo.presentation;

import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchJobTestController {

    private final SchedulerFactoryBean reportScheduler;
    private final SchedulerFactoryBean badgeScheduler;
    private final JobDetail reportBatchJobDetail;
    private final JobDetail badgeBatchJobDetail;

    public BatchJobTestController(
            @Qualifier("reportScheduler") SchedulerFactoryBean reportScheduler,
            @Qualifier("badgeScheduler") SchedulerFactoryBean badgeScheduler,
            @Qualifier("reportBatchJobDetail") JobDetail reportBatchJobDetail,
            @Qualifier("badgeBatchJobDetail") JobDetail badgeBatchJobDetail) {
        this.reportScheduler = reportScheduler;
        this.badgeScheduler = badgeScheduler;
        this.reportBatchJobDetail = reportBatchJobDetail;
        this.badgeBatchJobDetail = badgeBatchJobDetail;
    }

    @PostMapping("/report")
    public ResponseEntity<String> triggerReportJob() {
        try {
            Scheduler scheduler = reportScheduler.getScheduler();
            Trigger trigger = newTrigger()
                    .forJob(reportBatchJobDetail)
                    .startNow()
                    .build();
            scheduler.scheduleJob(trigger);
            return ResponseEntity.ok("report batch job success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("report batch job error: " + e.getMessage());
        }
    }

    @PostMapping("/badge")
    public ResponseEntity<String> triggerBadgeJob() {
        try {
            Scheduler scheduler = badgeScheduler.getScheduler();
            Trigger trigger = newTrigger()
                    .forJob(badgeBatchJobDetail)
                    .startNow()
                    .build();
            scheduler.scheduleJob(trigger);
            return ResponseEntity.ok("badge batch job success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("badge batch job error: " + e.getMessage());
        }
    }
}
