package com.example.demo.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.domain.model.Habit;
import com.example.demo.infrastructure.entity.HabitEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BadgeProcessorTest {

    @DisplayName("HabitEntity 로 습관의 뱃지를 최신 추적 정보를 바탕으로 업데이트한다.")
    @Test
    void process() throws Exception {
        // given
        TestHabit givenHabit = new TestHabit();
        TestHabitEntity givenEntity = new TestHabitEntity(givenHabit);
        BadgeProcessor processor = new BadgeProcessor();

        // when
        Habit result = processor.process(givenEntity);

        // then
        assertThat(result).isInstanceOf(givenHabit.getClass());
        assertThat(givenHabit.updateBadgeLevelCalled).isTrue();
    }

    class TestHabitEntity extends HabitEntity {
        private Habit habit;

        TestHabitEntity(Habit habit) {
            this.habit = habit;
        }

        @Override
        public Habit toHabit() {
            return habit;
        }
    }

    class TestHabit extends Habit {
        boolean updateBadgeLevelCalled = false;

        @Override
        public void updateBadgeLevel() {
            updateBadgeLevelCalled = true;
        }
    }
}
