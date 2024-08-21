package com.example.demo.application.schedular;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = SchedularConfig.class)
@TestPropertySource(properties = {"batch.cron.expression=0 0 12 * * ?"})
class SchedularConfigTest {

    @Autowired
    private SchedularConfig schedularConfig;

    @Test
    void testBatchJobDetail() {
        // given && when
        JobDetail jobDetail = schedularConfig.batchJobDetail();

        // then
        assertNotNull(jobDetail);
        assertEquals("batchJob", jobDetail.getKey().getName());
        assertEquals("batchGroup", jobDetail.getKey().getGroup());
        assertTrue(jobDetail.isDurable());
        assertEquals(BatchJobExecutor.class, jobDetail.getJobClass());
    }

    @Test
    void testBatchJobTrigger() {
        // given && when
        JobDetail jobDetail = schedularConfig.batchJobDetail();
        Trigger trigger = schedularConfig.batchJobTrigger(jobDetail);

        // then
        assertNotNull(trigger);
        assertEquals("batchTrigger", trigger.getKey().getName());
        assertEquals("batchGroup", trigger.getKey().getGroup());

        assertInstanceOf(CronTrigger.class, trigger);
        CronTrigger cronTrigger = (CronTrigger) trigger;
        assertEquals("0 0 12 * * ?", cronTrigger.getCronExpression());
    }

    @Test
    void testScheduler() throws SchedulerException {
        // given
        JobDetail jobDetail = schedularConfig.batchJobDetail();
        Trigger trigger = schedularConfig.batchJobTrigger(jobDetail);

        // when
        SchedulerFactoryBean schedulerFactoryBean = schedularConfig.scheduler(trigger, jobDetail);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        // then
        assertNotNull(schedulerFactoryBean);
        assertNotNull(scheduler);
        assertTrue(scheduler.checkExists(jobDetail.getKey()));
        assertTrue(scheduler.checkExists(trigger.getKey()));
    }
}
