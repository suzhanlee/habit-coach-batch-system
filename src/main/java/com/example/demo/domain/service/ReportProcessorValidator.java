package com.example.demo.domain.service;

import com.example.demo.dto.ReportData;
import com.example.demo.dto.ReportData.HabitReportData;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class ReportProcessorValidator implements Validator<ReportData> {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Override
    public boolean isValid(ReportData reportData) {
        if (reportData == null) {
            return false;
        }

        if (reportData.getUserId() == null || reportData.getUserName() == null || reportData.getEmail() == null) {
            return false;
        }

        if (!EMAIL_PATTERN.matcher(reportData.getEmail()).matches()) {
            return false;
        }

        if (reportData.getHabitReportDatas() == null) {
            return false;
        }

        for (HabitReportData habitReportData : reportData.getHabitReportDatas()) {
            if (isValidHabitReportData(habitReportData)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidHabitReportData(HabitReportData habitReportData) {
        return habitReportData.getHabitName() == null || habitReportData.getQuestions() == null
                || habitReportData.getAnswers() == null || habitReportData.getFeedback() == null;
    }
}
