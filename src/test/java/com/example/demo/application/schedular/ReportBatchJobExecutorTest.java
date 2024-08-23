package com.example.demo.application.schedular;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.Calendar;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

class ReportBatchJobExecutorTest {

    private FakeJobLauncher jobLauncher;
    private FakeJob batchJob;
    private ReportBatchJobExecutor reportBatchJobExecutor;
    private FakeJobExecutionContext jobExecutionContext;

    @BeforeEach
    void setUp() {
        jobLauncher = new FakeJobLauncher();
        batchJob = new FakeJob();
        reportBatchJobExecutor = new ReportBatchJobExecutor(jobLauncher, batchJob);
        jobExecutionContext = new FakeJobExecutionContext();
    }

    @Test
    void execute_shouldLaunchBatchJobWithCorrectParameters() {
        // given
        long beforeExecutionTime = System.currentTimeMillis();

        // when
        reportBatchJobExecutor.execute(jobExecutionContext);

        // then
        long afterExecutionTime = System.currentTimeMillis();

        assertTrue(jobLauncher.wasRunCalled);
        assertSame(batchJob, jobLauncher.lastJob);

        JobParameters jobParameters = jobLauncher.lastJobParameters;
        assertNotNull(jobParameters);

        Long timeParameter = jobParameters.getLong("time");
        assertNotNull(timeParameter);
        assertTrue(timeParameter >= beforeExecutionTime && timeParameter <= afterExecutionTime);
    }

    @Test
    void execute_shouldHandleExceptionGracefully() {
        // given
        jobLauncher.shouldThrowException = true;

        // when & then
        assertDoesNotThrow(() -> reportBatchJobExecutor.execute(jobExecutionContext));
    }

    private static class FakeJobLauncher implements JobLauncher {
        boolean wasRunCalled = false;
        Job lastJob = null;
        JobParameters lastJobParameters = null;
        boolean shouldThrowException = false;

        @Override
        public JobExecution run(Job job, JobParameters jobParameters) {
            if (shouldThrowException) {
                try {
                    throw new JobExecutionException("Test exception");
                } catch (JobExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
            wasRunCalled = true;
            lastJob = job;
            lastJobParameters = jobParameters;
            return new JobExecution(1L);
        }
    }

    private static class FakeJob implements Job {
        @Override
        public String getName() {
            return "fakeJob";
        }

        @Override
        public void execute(JobExecution execution) {
            System.out.println("FakeJob.execute");
        }
    }

    private static class FakeJobExecutionContext implements JobExecutionContext {
        @Override
        public Scheduler getScheduler() {
            return null;
        }

        @Override
        public Trigger getTrigger() {
            return null;
        }

        @Override
        public Calendar getCalendar() {
            return null;
        }

        @Override
        public boolean isRecovering() {
            return false;
        }

        @Override
        public TriggerKey getRecoveringTriggerKey() throws IllegalStateException {
            return null;
        }

        @Override
        public int getRefireCount() {
            return 0;
        }

        @Override
        public JobDataMap getMergedJobDataMap() {
            return null;
        }

        @Override
        public JobDetail getJobDetail() {
            return null;
        }

        @Override
        public org.quartz.Job getJobInstance() {
            return null;
        }

        @Override
        public Date getFireTime() {
            return null;
        }

        @Override
        public Date getScheduledFireTime() {
            return null;
        }

        @Override
        public Date getPreviousFireTime() {
            return null;
        }

        @Override
        public Date getNextFireTime() {
            return null;
        }

        @Override
        public String getFireInstanceId() {
            return "";
        }

        @Override
        public Object getResult() {
            return null;
        }

        @Override
        public void setResult(Object o) {

        }

        @Override
        public long getJobRunTime() {
            return 0;
        }

        @Override
        public void put(Object o, Object o1) {

        }

        @Override
        public Object get(Object o) {
            return null;
        }
    }
}
