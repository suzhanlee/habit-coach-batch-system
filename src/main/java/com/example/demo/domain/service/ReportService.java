package com.example.demo.domain.service;

import com.example.demo.domain.model.User;
import com.example.demo.dto.ReportData;

public interface ReportService {
    ReportData createReportData(User user, Long reportTime);

    String generateReport(ReportData reportData);
}
