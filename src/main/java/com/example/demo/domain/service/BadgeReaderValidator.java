package com.example.demo.domain.service;

import com.example.demo.infrastructure.entity.HabitEntity;

public class BadgeReaderValidator implements Validator<HabitEntity> {

    @Override
    public boolean isValid(HabitEntity habitEntity) {
        if (habitEntity == null) {
            return false;
        }

        if (habitEntity.getId() == null || habitEntity.getBadge() == null || habitEntity.getName() == null
                || habitEntity.getFormationStage() == null || habitEntity.getDescription() == null
                || habitEntity.getUser() == null) {
            return false;
        }

        return habitEntity.getTrackings() != null;
    }
}
