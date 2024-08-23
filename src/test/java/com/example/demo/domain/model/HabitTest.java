package com.example.demo.domain.model;

import static com.example.demo.domain.model.Badge.CHALLENGER;
import static com.example.demo.domain.model.Badge.SILVER;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HabitTest {

    @Test
    @DisplayName("습관의 뱃지 레벨을 최대 연속 일수에 맞게 업데이트한다.")
    void update_badge_level() {
        // given
        Habit habit = new Habit(1L, "exercise", "do exercise",
                new HabitFormationStage(1L, 2),
                createGivenTrackings(),
                SILVER);

        // when
        habit.updateBadgeLevel();

        // then
        assertThat(habit.getBadge()).isEqualTo(Badge.DIAMOND);
    }

    @Test
    @DisplayName("현재 배지보다 낮은 티어의 배지로는 업데이트되지 않는다.")
    void do_not_update_to_lower_badge() {
        // given
        Habit habit = new Habit(1L, "exercise", "do exercise",
                new HabitFormationStage(1L, 2),
                createGivenTrackings(50),
                CHALLENGER);

        // when
        habit.updateBadgeLevel();

        // then
        assertThat(habit.getBadge()).isEqualTo(CHALLENGER);
    }

    private List<HabitTracking> createGivenTrackings() {
        return createGivenTrackings(100);
    }

    private List<HabitTracking> createGivenTrackings(int days) {
        return IntStream.rangeClosed(1, days)
                .mapToObj(i -> new HabitTracking((long) i, LocalDate.of(2024, 8, 23).plusDays(i)))
                .toList();
    }
}
