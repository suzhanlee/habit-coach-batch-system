package com.example.demo.domain.model;

import java.util.List;
import lombok.Getter;

@Getter
public class Habit {

    private Long id;
    private String name;
    private String description;
    private User user;
    private HabitFormationStage formationStage;
    private HabitTrackings trackings;
    private Badge badge;

    public Habit(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Habit(Long id, String name, String description, HabitFormationStage formationStage,
                 List<HabitTracking> trackings) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.formationStage = formationStage;
        this.trackings = new HabitTrackings(trackings);
    }

    public Habit(Long id, String name, String description, HabitFormationStage formationStage,
                 HabitTrackings trackings) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.formationStage = formationStage;
        this.trackings = trackings;
    }

    public void updateBadgeLevel() {
        int currenMaxStreak = this.trackings.countMaxStreak();
        this.badge = Badge.findBadgeByStreak(currenMaxStreak);
    }
}
