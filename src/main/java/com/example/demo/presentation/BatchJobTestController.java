package com.example.demo.presentation;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchJobTestController {

    private final JobLauncher jobLauncher;
    private final Job reportBatchJob;
    private final Job badgeBatchJob;

    public BatchJobTestController(JobLauncher jobLauncher,
                                  @Qualifier("reportBatchJob") Job reportBatchJob,
                                  @Qualifier("badgeBatchJob") Job badgeBatchJob) {
        this.jobLauncher = jobLauncher;
        this.reportBatchJob = reportBatchJob;
        this.badgeBatchJob = badgeBatchJob;
    }

    @PostMapping("/report")
    public ResponseEntity<String> triggerReportJob() {
        try {
            jobLauncher.run(reportBatchJob, createJobParameters());
            return ResponseEntity.ok("report batch job success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("report batch job error: " + e.getMessage());
        }
    }

    @PostMapping("/badge")
    public ResponseEntity<String> triggerBadgeJob() {
        try {
            JobParameters jobParameters = createJobParameters();
            jobLauncher.run(badgeBatchJob, jobParameters);
            return ResponseEntity.ok("badge batch job success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("badge batch job error: " + e.getMessage());
        }
    }

    private JobParameters createJobParameters() {
        return new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
    }
}
