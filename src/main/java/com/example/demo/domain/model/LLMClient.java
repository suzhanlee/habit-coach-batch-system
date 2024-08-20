package com.example.demo.domain.model;

import com.example.demo.dto.ReportData;

public interface LLMClient {

    String generateReport(ReportData reportData);
}
