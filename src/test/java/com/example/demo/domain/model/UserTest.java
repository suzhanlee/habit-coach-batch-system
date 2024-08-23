package com.example.demo.domain.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("사용자가 가진 습관의 뱃지 티어를 업데이트한다.")
    void refresh_habit_tiers() {
        // given
        TestHabit soccer = new TestHabit(1L, "soccer", "do soccer");
        TestHabit baseball = new TestHabit(2L, "baseball", "do baseball");
        List<Habit> habits = Arrays.asList(soccer, baseball);
        User user = new User(1L, "suchan", "wlscww@example.com", habits);

        // when
        user.refreshHabitTiers();

        // then
        assertTrue(soccer.wasUpdateBadgeLevelCalled());
        assertTrue(baseball.wasUpdateBadgeLevelCalled());
    }

    private static class TestHabit extends Habit {
        private boolean updateBadgeLevelCalled = false;

        public TestHabit(Long id, String name, String description) {
            super(id, name, description);
        }

        @Override
        public void updateBadgeLevel() {
            updateBadgeLevelCalled = true;
        }

        public boolean wasUpdateBadgeLevelCalled() {
            return updateBadgeLevelCalled;
        }
    }
}
