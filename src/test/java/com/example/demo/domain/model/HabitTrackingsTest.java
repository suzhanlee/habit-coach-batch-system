package com.example.demo.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class HabitTrackingsTest {

    @Test
    @DisplayName("습관 추적이 없는 경우")
    void count_empty_trackings() {
        // given
        HabitTrackings habitTrackings = new HabitTrackings(new ArrayList<>());

        // when
        int result = habitTrackings.countMaxStreak();

        // then
        assertEquals(0, result);
    }

    @ParameterizedTest
    @MethodSource("provideTrackingsAndExpectedMaxStreak")
    @DisplayName("습관 추적이 있는 경우 연속된 스트릭 수를 계산한다.")
    void count_max_streak(List<HabitTracking> trackings, int expectedMaxStreak) {
        // given
        HabitTrackings habitTrackings = new HabitTrackings(trackings);

        // when
        int result = habitTrackings.countMaxStreak();

        // then
        assertEquals(expectedMaxStreak, result);
    }

    private static Stream<Arguments> provideTrackingsAndExpectedMaxStreak() {
        return Stream.of(
                Arguments.of(
                        List.of(new HabitTracking(1L, LocalDate.of(2023, 5, 1))), 1
                ),
                Arguments.of(
                        List.of(
                                new HabitTracking(1L, LocalDate.of(2023, 5, 2)),
                                new HabitTracking(2L, LocalDate.of(2023, 5, 1))
                        ), 2
                ),
                Arguments.of(
                        List.of(
                                new HabitTracking(1L, LocalDate.of(2023, 5, 5)),
                                new HabitTracking(2L, LocalDate.of(2023, 5, 4)),
                                new HabitTracking(3L, LocalDate.of(2023, 5, 3)),
                                new HabitTracking(4L, LocalDate.of(2023, 5, 1))
                        ), 3
                ),
                Arguments.of(
                        List.of(
                                new HabitTracking(1L, LocalDate.of(2023, 5, 10)),
                                new HabitTracking(2L, LocalDate.of(2023, 5, 9)),
                                new HabitTracking(3L, LocalDate.of(2023, 5, 8)),
                                new HabitTracking(4L, LocalDate.of(2023, 5, 5)),
                                new HabitTracking(5L, LocalDate.of(2023, 5, 4)),
                                new HabitTracking(6L, LocalDate.of(2023, 5, 1))
                        ), 3
                )
        );
    }
}
