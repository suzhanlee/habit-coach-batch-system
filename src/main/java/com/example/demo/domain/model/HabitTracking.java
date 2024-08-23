package com.example.demo.domain.model;

import java.time.LocalDate;

public class HabitTracking {

    private Long id;
    private Habit habit;
    private LocalDate completedDate;

    public HabitTracking(Long id, Habit habit, LocalDate completedDate) {
        this.id = id;
        this.habit = habit;
        this.completedDate = completedDate;
    }

    public HabitTracking(Long id, LocalDate completedDate) {
        this.id = id;
        this.completedDate = completedDate;
    }

    public int compareCompletedDate(HabitTracking habitTracking) {
        return this.completedDate.compareTo(habitTracking.completedDate);
    }

    public boolean isContinuousDate(HabitTracking habitTracking) {
        return this.completedDate.minusDays(1).equals(habitTracking.completedDate);
    }
}
