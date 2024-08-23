package com.example.demo.application.schedular;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Component(value = "reportBatchJobExecutor")
public class ReportBatchJobExecutor implements Job {

    private final JobLauncher jobLauncher;
    private final org.springframework.batch.core.Job batchJob;

    public ReportBatchJobExecutor(JobLauncher jobLauncher, org.springframework.batch.core.Job batchJob) {
        this.jobLauncher = jobLauncher;
        this.batchJob = batchJob;
    }

    @Override
    public void execute(JobExecutionContext context) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(batchJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
