package com.example.demo.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportData {

    private Long userId;
    private String userName;
    private String email;
    private List<HabitReportData> habitReportDatas;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HabitReportData {
        private String habitName;
        private int month;
        private int year;
        private int totalCompletedDays;
        private int formationStage;
        private List<String> questions;
        private List<String> answers;
        private String feedback;
    }
}
