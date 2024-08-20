package com.example.demo.domain.model;

import com.example.demo.dto.ReportData;

public class GptClient implements LLMClient {

    @Override
    public String generateReport(ReportData reportData) {
        return "report content";
    }
}
