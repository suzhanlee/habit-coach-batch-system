package com.example.demo.infrastructure.entity;

import com.example.demo.domain.model.Habit;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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

    public HabitEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Habit toHabit() {
        return new Habit(id, name, description);
    }

    public void addUserEntity(UserEntity userEntity) {
        this.user = userEntity;
        userEntity.addHabitEntity(this);
    }

    public void addHabitFormationStageEntity(HabitFormationStageEntity habitFormationStageEntity) {
        this.formationStage = habitFormationStageEntity;
    }

    public void addHabitTrackingEntity(HabitTrackingEntity habitTrackingEntity) {
        this.trackings.add(habitTrackingEntity);
    }
}
