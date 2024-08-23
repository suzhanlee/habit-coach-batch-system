package com.example.demo.domain.service;

import com.example.demo.dto.ReportData;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.core.task.AsyncTaskExecutor;

@Slf4j
public class ReportWriter implements ItemWriter<ReportData> {

    private final ReportService reportService;
    private final EmailService emailService;
    private final AsyncTaskExecutor taskExecutor;

    public ReportWriter(ReportService reportService, EmailService emailService, AsyncTaskExecutor taskExecutor) {
        this.reportService = reportService;
        this.emailService = emailService;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void write(Chunk<? extends ReportData> chunk) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (ReportData reportData : chunk.getItems()) {
            futures.add(CompletableFuture.runAsync(() -> {
                String report = reportService.generateReport(reportData);
                emailService.sendEmail(reportData.getEmail(), "Monthly Habit Report", report);
            }, taskExecutor));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }
}
