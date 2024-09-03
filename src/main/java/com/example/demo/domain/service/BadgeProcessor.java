package com.example.demo.domain.service;

import com.example.demo.domain.model.Habit;
import com.example.demo.infrastructure.entity.HabitEntity;
import org.springframework.batch.item.ItemProcessor;

public class BadgeProcessor implements ItemProcessor<HabitEntity, Habit> {

    @Override
    public Habit process(HabitEntity entity) throws Exception {
        Habit habit  = entity.toHabit();
        habit.updateBadgeLevel();
        return habit;
    }
}
