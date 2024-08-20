package com.example.demo.entity;

import com.example.demo.domain.Habit;
import com.example.demo.domain.HabitTracking;
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

    public HabitTracking toHabitTracking(Habit habit) {
        return new HabitTracking(id, habit, completedDate);
    }
}
