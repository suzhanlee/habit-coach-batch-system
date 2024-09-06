package com.example.demo.domain.service;

import com.example.demo.domain.model.Habit;
import com.example.demo.infrastructure.entity.HabitEntity;
import org.springframework.batch.item.ItemProcessor;

public class BadgeProcessor implements ItemProcessor<HabitEntity, Habit> {

    private final BadgeBatchContext badgeBatchContext;

    public BadgeProcessor(BadgeBatchContext badgeBatchContext) {
        this.badgeBatchContext = badgeBatchContext;
    }

    @Override
    public Habit process(HabitEntity entity) {
        Habit habit = entity.toHabit();
        habit.updateBadgeLevel();
        if (entity != null) {
            badgeBatchContext.incrementHabitCnt();
            badgeBatchContext.plusTrackingsCnt(entity.getTrackings().size());
        }
        return habit;
    }
}
