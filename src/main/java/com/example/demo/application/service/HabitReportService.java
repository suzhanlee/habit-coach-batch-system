package com.example.demo.application.service;

import com.example.demo.domain.model.Habit;
import com.example.demo.domain.model.HabitFormationStage;
import com.example.demo.domain.model.LLMClient;
import com.example.demo.domain.model.User;
import com.example.demo.domain.repository.HabitRepository;
import com.example.demo.domain.service.ReportService;
import com.example.demo.dto.ReportData;
import com.example.demo.dto.ReportData.HabitReportData;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HabitReportService implements ReportService {

    private final HabitRepository habitRepository;
    private final LLMClient llmClient;

    public HabitReportService(HabitRepository habitRepository, LLMClient llmClient) {
        this.habitRepository = habitRepository;
        this.llmClient = llmClient;
    }

    @Override
    public ReportData createReportData(User user, Long reportTime) {
        YearMonth reportMonth = extractReportTime(reportTime);
        List<HabitReportData> habitReportDataList = createHabitReportDatas(user, reportMonth);
        return new ReportData(user.getId(), user.getName(), user.getEmail(), habitReportDataList);
    }

    private YearMonth extractReportTime(Long reportTime) {
        return YearMonth.from(Instant.ofEpochMilli(reportTime).atZone(ZoneId.systemDefault()));
    }

    private List<HabitReportData> createHabitReportDatas(User user, YearMonth currentMonth) {
        return user.getHabits().stream()
                .map(habit -> createHabitReportData(currentMonth, habit))
                .toList();
    }

    private HabitReportData createHabitReportData(YearMonth currentMonth, Habit habit) {
        HabitFormationStage formationStage = habit.getFormationStage();

        return new HabitReportData(
                habit.getName(),
                currentMonth.getMonthValue(),
                currentMonth.getYear(),
                habit.getTrackings().size(),
                formationStage.getStage(),
                formationStage.getQuestions(),
                formationStage.getAnswers(),
                formationStage.getFeedback()
        );
    }

    @Override
    public String generateReport(ReportData reportData) {
        return llmClient.generateReport(reportData);
    }
}
