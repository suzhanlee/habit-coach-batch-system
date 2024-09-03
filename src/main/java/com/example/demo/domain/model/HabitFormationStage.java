package com.example.demo.domain.model;

import java.util.List;
import lombok.Getter;

@Getter
public class HabitFormationStage {

    private Long id;
    private int stage;
    private List<String> questions;
    private List<String> answers;
    private String feedback;

    public HabitFormationStage(Long id, int stage, List<String> questions, List<String> answers, String feedback) {
        this.id = id;
        this.stage = stage;
        this.questions = questions;
        this.answers = answers;
        this.feedback = feedback;
    }

    public HabitFormationStage(Long id, int stage) {
        this.id = id;
        this.stage = stage;
    }
}
