package com.example.demo.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.domain.model.User;
import com.example.demo.dto.ReportData;
import com.example.demo.infrastructure.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReportProcessorTest {

    @Test
    @DisplayName("reportService의 createReportData 메소드가 호출되었는지 검증한다.")
    void process_report_data() {
        // given
        FakeReportService reportService = new FakeReportService();
        ReportProcessor reportProcessor = new ReportProcessor(reportService, System.currentTimeMillis(),
                new ReportProcessorValidator(), new ReportProcessorReportBatchContextManager());
        UserEntity givenUserEntity = new UserEntity();

        // when
        ReportData result = reportProcessor.process(givenUserEntity);

        // then
        assertThat(result).isNotNull();
        assertThat(reportService.isCalled).isTrue();
    }

    private static class FakeReportService implements ReportService {
        private boolean isCalled;

        public FakeReportService() {
            this.isCalled = false;
        }

        @Override
        public ReportData createReportData(User user, Long reportTime) {
            this.isCalled = true;
            return new ReportData();
        }

        @Override
        public String generateReport(ReportData reportData) {
            return "monthly habit report";
        }
    }
}
