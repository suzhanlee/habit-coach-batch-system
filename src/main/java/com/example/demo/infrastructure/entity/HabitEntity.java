package com.example.demo.infrastructure.entity;

import com.example.demo.domain.model.Badge;
import com.example.demo.domain.model.Habit;
import com.example.demo.domain.model.HabitTracking;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "habits")
@NoArgsConstructor
@Getter
public class HabitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
    private HabitFormationStageEntity formationStage;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HabitTrackingEntity> trackings = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Badge badge;

    public HabitEntity(String name, String description) {
        this.name = name;
        this.description = description;
        this.badge = Badge.UN_RANK;
    }

    public HabitEntity(Long id, String name, String description, HabitFormationStageEntity formationStage,
                       List<HabitTrackingEntity> trackings) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.formationStage = formationStage;
        this.trackings = trackings;
        this.badge = Badge.UN_RANK;
    }

    public Habit toHabit() {
        return new Habit(
                id,
                name,
                description,
                formationStage.toFormationStage(),
                trackings.stream().map(HabitTrackingEntity::toHabitTracking).toList(),
                badge
        );
    }

    public static HabitEntity fromHabit(Habit habit) {
        HabitEntity habitEntity = new HabitEntity();
        habitEntity.id = habit.getId();
        habitEntity.name = habit.getName();
        habitEntity.description = habit.getDescription();
        habitEntity.badge = habit.getBadge();

        HabitFormationStageEntity habitFormationStageEntity = HabitFormationStageEntity.fromHabitFormationStage(habit.getFormationStage());
        habitEntity.addHabitFormationStage(habitFormationStageEntity);

        for (HabitTracking habitTracking : habit.getTrackings().habitTrackings()) {
            HabitTrackingEntity habitTrackingEntity = HabitTrackingEntity.fromHabitTracking(habitTracking);
            habitEntity.addHabitTrackingEntity(habitTrackingEntity);
        }

        return habitEntity;
    }

    public void addUserEntity(UserEntity userEntity) {
        this.user = userEntity;
    }

    public void addHabitFormationStage(HabitFormationStageEntity habitFormationStageEntity) {
        this.formationStage = habitFormationStageEntity;
        habitFormationStageEntity.addHabitEntity(this);
    }

    public void addHabitTrackingEntity(HabitTrackingEntity habitTrackingEntity) {
        this.trackings.add(habitTrackingEntity);
        habitTrackingEntity.addHabitEntity(this);
    }
}
