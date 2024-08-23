package com.example.demo.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HabitTrackingTest {

    @Test
    @DisplayName("두 날짜 중 어느 날짜가 더 과거 날인지 알려준다.")
    void compare_completed_date() {
        // given
        HabitTracking tracking1 = new HabitTracking(1L, LocalDate.of(2023, 5, 1));
        HabitTracking tracking2 = new HabitTracking(2L, LocalDate.of(2023, 5, 2));
        HabitTracking tracking3 = new HabitTracking(3L, LocalDate.of(2023, 5, 1));

        // when
        int resultBefore = tracking1.compareCompletedDate(tracking2);
        int resultAfter = tracking2.compareCompletedDate(tracking1);
        int resultEqual = tracking1.compareCompletedDate(tracking3);

        // then
        assertTrue(resultBefore < 0);
        assertTrue(resultAfter > 0);
        assertEquals(0, resultEqual);
    }

    @Test
    @DisplayName("습관 추적한 날이 연속된 날인지 확인한다.")
    void is_continuous_date() {
        // given
        HabitTracking tracking1 = new HabitTracking(1L, LocalDate.of(2023, 5, 1));
        HabitTracking tracking2 = new HabitTracking(2L, LocalDate.of(2023, 5, 2));
        HabitTracking tracking3 = new HabitTracking(3L, LocalDate.of(2023, 5, 3));

        // when
        boolean isContinuous1 = tracking2.isContinuousDate(tracking1);
        boolean isContinuous2 = tracking3.isContinuousDate(tracking2);
        boolean isNotContinuous = tracking3.isContinuousDate(tracking1);

        // then
        assertTrue(isContinuous1);
        assertTrue(isContinuous2);
        assertFalse(isNotContinuous);
    }
}
