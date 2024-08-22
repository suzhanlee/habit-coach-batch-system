package com.example.demo.dto;

import java.util.List;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReportData that = (ReportData) o;
        return Objects.equals(userId, that.userId) && Objects.equals(userName, that.userName)
                && Objects.equals(email, that.email) && Objects.equals(habitReportDatas,
                that.habitReportDatas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, email, habitReportDatas);
    }

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

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            HabitReportData that = (HabitReportData) o;
            return month == that.month && year == that.year && totalCompletedDays == that.totalCompletedDays
                    && formationStage == that.formationStage && Objects.equals(habitName, that.habitName)
                    && Objects.equals(questions, that.questions) && Objects.equals(answers, that.answers)
                    && Objects.equals(feedback, that.feedback);
        }

        @Override
        public int hashCode() {
            return Objects.hash(habitName, month, year, totalCompletedDays, formationStage, questions, answers,
                    feedback);
        }
    }
}
