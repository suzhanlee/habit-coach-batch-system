package com.example.demo.domain;

import com.example.demo.dto.ReportData;

public interface LLMClient {

    String generateReport(ReportData reportData);
}
