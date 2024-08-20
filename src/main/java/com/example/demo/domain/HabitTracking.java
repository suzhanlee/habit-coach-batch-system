package com.example.demo.domain;

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
}
