package com.example.demo.domain.service;

import com.example.demo.dto.ReportData;
import com.example.demo.infrastructure.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class ReportProcessor implements ItemProcessor<UserEntity, ReportData> {

    private final ReportService reportService;
    private final Long reportTime;
    private final Validator validator;
    private final ReportBatchContext reportBatchContext;

    public ReportProcessor(ReportService reportService, Long reportTime, Validator validator,
                           ReportBatchContext reportBatchContext) {
        this.reportService = reportService;
        this.reportTime = reportTime;
        this.validator = validator;
        this.reportBatchContext = reportBatchContext;
    }

    @Override
    public ReportData process(UserEntity userEntity) {
        ReportData reportData = reportService.createReportData(userEntity.toUser(), reportTime);
        if (reportData != null && !validator.isValid(reportData)) {
            throw new IllegalStateException("validate error : reportData의 형식이 올바르지 않습니다.");
        }
        if (reportData != null) {
            reportBatchContext.incrementUserCnt();
            reportBatchContext.plusHabitCnt(reportData.getHabitReportDatas().size());
        }
        return reportData;
    }
}
