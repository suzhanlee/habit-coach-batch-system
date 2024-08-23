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

@SpringBootTest(classes = ReportSchedularConfig.class)
@TestPropertySource(properties = {"batch.report.cron.expression=0 0 12 * * ?"})
class ReportSchedularConfigTest {

    @Autowired
    private ReportSchedularConfig reportSchedularConfig;

    @Test
    void testReportBatchJobDetail() {
        // given && when
        JobDetail jobDetail = reportSchedularConfig.reportBatchJobDetail();

        // then
        assertNotNull(jobDetail);
        assertEquals("reportBatchJob", jobDetail.getKey().getName());
        assertEquals("reportBatchGroup", jobDetail.getKey().getGroup());
        assertTrue(jobDetail.isDurable());
        assertEquals(ReportBatchJobExecutor.class, jobDetail.getJobClass());
    }

    @Test
    void testBatchJobTrigger() {
        // given && when
        JobDetail jobDetail = reportSchedularConfig.reportBatchJobDetail();
        Trigger trigger = reportSchedularConfig.reportBatchJobTrigger(jobDetail);

        // then
        assertNotNull(trigger);
        assertEquals("reportBatchTrigger", trigger.getKey().getName());
        assertEquals("reportBatchGroup", trigger.getKey().getGroup());

        assertInstanceOf(CronTrigger.class, trigger);
        CronTrigger cronTrigger = (CronTrigger) trigger;
        assertEquals("0 0 12 * * ?", cronTrigger.getCronExpression());
    }

    @Test
    void testScheduler() throws SchedulerException {
        // given
        JobDetail jobDetail = reportSchedularConfig.reportBatchJobDetail();
        Trigger trigger = reportSchedularConfig.reportBatchJobTrigger(jobDetail);

        // when
        SchedulerFactoryBean schedulerFactoryBean = reportSchedularConfig.reportScheduler(trigger, jobDetail);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        // then
        assertNotNull(schedulerFactoryBean);
        assertNotNull(scheduler);
        assertTrue(scheduler.checkExists(jobDetail.getKey()));
        assertTrue(scheduler.checkExists(trigger.getKey()));
    }
}
