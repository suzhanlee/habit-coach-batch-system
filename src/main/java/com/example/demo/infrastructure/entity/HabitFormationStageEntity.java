package com.example.demo.infrastructure.entity;

import com.example.demo.domain.model.HabitFormationStage;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "habit_formation_stages")
@NoArgsConstructor
@Getter
public class HabitFormationStageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id")
    private HabitEntity habit;

    @ElementCollection
    private List<String> questions;

    @ElementCollection
    private List<String> answers;

    private String feedback;

    public HabitFormationStageEntity(int stage, List<String> questions, List<String> answers, String feedback) {
        this.stage = stage;
        this.questions = questions;
        this.answers = answers;
        this.feedback = feedback;
    }

    public static HabitFormationStageEntity fromHabitFormationStage(HabitFormationStage formationStage) {
        HabitFormationStageEntity stageEntity = new HabitFormationStageEntity();
        stageEntity.id = formationStage.getId();
        stageEntity.stage = formationStage.getStage();
        stageEntity.questions = formationStage.getQuestions();
        stageEntity.answers = formationStage.getAnswers();
        stageEntity.feedback = formationStage.getFeedback();
        return stageEntity;
    }

    public HabitFormationStage toFormationStage() {
        return new HabitFormationStage(id, stage, questions, answers, feedback);
    }

    public void addHabitEntity(HabitEntity habitEntity) {
        this.habit = habitEntity;
    }
}
