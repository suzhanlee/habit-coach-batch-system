package com.example.demo.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.domain.model.Habit;
import com.example.demo.domain.model.HabitFormationStage;
import com.example.demo.domain.model.HabitTracking;
import com.example.demo.domain.model.LLMClient;
import com.example.demo.domain.model.User;
import com.example.demo.domain.repository.HabitRepository;
import com.example.demo.dto.ReportData;
import com.example.demo.dto.ReportData.HabitReportData;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReportServiceTest {

    @Test
    @DisplayName("user 정보를 바탕으로 reportData를 생성한다.")
    void create_habit_report_datas() {
        // given
        HabitReportService reportService = new HabitReportService(new FakeHabitRepository(), new FakeGptClient());
        User givenUser = createGivenUser();

        // when
        ReportData result = reportService.createReportData(givenUser, System.currentTimeMillis());

        // then
        assertThat(result).isEqualTo(
                new ReportData(1L, "suchan", "wlscww123@kakao.com",
                        List.of(new HabitReportData("exercise", LocalDate.now().getMonthValue(),
                                LocalDate.now().getYear(), 20, 2, List.of("question"), List.of("answer"), "feedback"))));
    }

    private User createGivenUser() {
        return new User(1L, "suchan", "wlscww123@kakao.com",
                List.of(new Habit(1L, "exercise", "do exercise",
                        new HabitFormationStage(1L, 2, List.of("question"), List.of("answer"), "feedback"),
                        createGivenHabitTrackings())
                ));
    }

    private List<HabitTracking> createGivenHabitTrackings() {
        return IntStream.rangeClosed(1, 20)
                .mapToObj(i -> new HabitTracking((long) i, LocalDate.of(LocalDate.now().getYear(), LocalDate.now()
                        .getMonthValue(), i))).toList();
    }

    @Test
    @DisplayName("월간 습관 평가 리포트를 생성한다.")
    void generate_report() {
        // given
        HabitReportService reportService = new HabitReportService(new FakeHabitRepository(), new FakeGptClient());
        ReportData givenReportData = new ReportData();

        // when
        String result = reportService.generateReport(givenReportData);

        // then
        assertThat(result).isEqualTo("fake report");
    }

    private static class FakeHabitRepository implements HabitRepository {
        @Override
        public List<Habit> findHabitList(Long userId) {
            return List.of(new Habit(1L, "독서", "책 읽기"));
        }
    }

    private static class FakeGptClient implements LLMClient {
        @Override
        public String generateReport(ReportData reportData) {
            return "fake report";
        }
    }
}
