package com.example.demo.domain.model;

import java.util.List;
import lombok.Getter;

@Getter
public class User {

    private Long id;
    private String name;
    private String email;
    private List<Habit> habits;

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User(Long id, String name, String email, List<Habit> habits) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.habits = habits;
    }

    public User(String name, String email, List<Habit> habits) {
        this.name = name;
        this.email = email;
        this.habits = habits;
    }

    public void refreshHabitTiers() {
        for (Habit habit : habits) {
            habit.updateBadgeLevel();
        }
    }
}
