package com.example.demo.domain.service;

import com.example.demo.application.service.ReportService;
import com.example.demo.dto.ReportData;
import com.example.demo.infrastructure.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class ReportProcessor implements ItemProcessor<UserEntity, ReportData> {

    private final ReportService reportService;
    private final Long reportTime;

    public ReportProcessor(ReportService reportService, Long reportTime) {
        this.reportService = reportService;
        this.reportTime = reportTime;
    }

    @Override
    public ReportData process(UserEntity userEntity) {
        return reportService.createReportData(userEntity.toUser(), reportTime);
    }
}
