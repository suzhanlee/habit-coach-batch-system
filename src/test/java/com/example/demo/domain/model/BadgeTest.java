package com.example.demo.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BadgeTest {

    @ParameterizedTest
    @MethodSource("provideStreakCountsAndExpectedBadges")
    @DisplayName("모든 경우에 대해 streak 수에 해당하는 뱃지를 찾아온다.")
    void find_badge_by_streak(int streakCount, Badge expectedBadge) {
        assertEquals(expectedBadge, Badge.findBadgeByStreak(streakCount));
    }

    private static Stream<Arguments> provideStreakCountsAndExpectedBadges() {
        return Stream.of(
                Arguments.of(0, Badge.UN_RANK),
                Arguments.of(1, Badge.UN_RANK),
                Arguments.of(4, Badge.UN_RANK),
                Arguments.of(5, Badge.BRONZE),
                Arguments.of(9, Badge.BRONZE),
                Arguments.of(10, Badge.SILVER),
                Arguments.of(29, Badge.SILVER),
                Arguments.of(30, Badge.GOLD),
                Arguments.of(65, Badge.GOLD),
                Arguments.of(66, Badge.PLATINUM),
                Arguments.of(99, Badge.PLATINUM),
                Arguments.of(100, Badge.DIAMOND),
                Arguments.of(299, Badge.DIAMOND),
                Arguments.of(300, Badge.CHALLENGER),
                Arguments.of(1000, Badge.CHALLENGER)
        );
    }
}
