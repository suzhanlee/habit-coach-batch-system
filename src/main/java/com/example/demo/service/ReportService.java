package com.example.demo.service;

import com.example.demo.domain.Habit;
import com.example.demo.domain.HabitFormationStage;
import com.example.demo.domain.LLMClient;
import com.example.demo.domain.User;
import com.example.demo.domain.repository.HabitRepository;
import com.example.demo.dto.ReportData;
import com.example.demo.dto.ReportData.HabitReportData;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReportService {

    private final HabitRepository habitRepository;
    private final LLMClient llmClient;

    public ReportService(HabitRepository habitRepository, LLMClient llmClient) {
        this.habitRepository = habitRepository;
        this.llmClient = llmClient;
    }

    public String generateReport(ReportData reportData) {
        return llmClient.generateReport(reportData);
    }

    public ReportData createReportData(User user, Long reportTime) {
        YearMonth reportMonth = extractReportTime(reportTime);
        List<HabitReportData> habitReportDataList = createHabitReportDatas(user.getId(), reportMonth);
        return new ReportData(user.getId(), user.getName(), user.getEmail(), habitReportDataList);
    }

    private YearMonth extractReportTime(Long reportTime) {
        return YearMonth.from(Instant.ofEpochMilli(reportTime).atZone(ZoneId.systemDefault()));
    }

    private List<HabitReportData> createHabitReportDatas(Long userId, YearMonth currentMonth) {
        // 이거 유저가 이미 fetch join으로 모두 가져와서 문제가 된다...
        // 다시 생각해보자.. 아래 쿼리들은 잘못된 쿼리 -> b/c 이미 User에 모든 정보가 다 있음
        // 그런데, 내가 필요한 건 특정 월의 totalTrackingSize 다.
        List<Habit> habits = habitRepository.findHabitList(userId);
        List<HabitReportData> habitReportDataList = new ArrayList<>();

        for (Habit habit : habits) {
            HabitReportData habitReportData = createHabitReportData(currentMonth, habit);
            habitReportDataList.add(habitReportData);
        }
        return habitReportDataList;
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
}
