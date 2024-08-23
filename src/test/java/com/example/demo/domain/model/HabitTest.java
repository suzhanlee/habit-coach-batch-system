package com.example.demo.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HabitTest {

    @Test
    @DisplayName("습관의 뱃지 레벨의 사용자 정보에 맞게 최신 버전으로 업데이트 한다.")
    void update_badge_level() {
        // given
        Habit habit = new Habit(1L, "exercise", "do exercise",
                new HabitFormationStage(1L, 2),
                createGivenTrackings());

        // when
        habit.updateBadgeLevel();

        // then
        assertThat(habit.getBadge()).isEqualTo(Badge.DIAMOND);
    }

    private List<HabitTracking> createGivenTrackings() {
        List<HabitTracking> habitTrackings = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            habitTrackings.add(new HabitTracking((long) i, LocalDate.of(2024, 8, 23).plusDays(i)));
        }
        return habitTrackings;
    }
}
