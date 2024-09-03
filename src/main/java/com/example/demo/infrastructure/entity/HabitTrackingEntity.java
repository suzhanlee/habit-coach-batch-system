package com.example.demo.infrastructure.entity;

import com.example.demo.domain.model.HabitTracking;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "habit_trackings")
@NoArgsConstructor
@Getter
public class HabitTrackingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id")
    private HabitEntity habit;

    private LocalDate completedDate;

    public HabitTrackingEntity(HabitEntity habit, LocalDate completedDate) {
        this.habit = habit;
        this.completedDate = completedDate;
    }

    public static HabitTrackingEntity fromHabitTracking(HabitTracking tracking) {
        HabitTrackingEntity habitTrackingEntity = new HabitTrackingEntity();
        habitTrackingEntity.id = tracking.getId();
        habitTrackingEntity.completedDate = tracking.getCompletedDate();
        return habitTrackingEntity;
    }

    public HabitTracking toHabitTracking() {
        return new HabitTracking(id, completedDate);
    }

    public void addHabitEntity(HabitEntity habitEntity) {
        this.habit = habitEntity;
    }
}
