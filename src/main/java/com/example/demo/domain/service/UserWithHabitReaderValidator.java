package com.example.demo.domain.service;

import com.example.demo.domain.model.Badge;
import com.example.demo.infrastructure.entity.HabitEntity;
import com.example.demo.infrastructure.entity.UserEntity;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class UserWithHabitReaderValidator implements Validator<UserEntity> {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Override
    public boolean isValid(UserEntity userEntity) {
        if (userEntity == null) {
            return false;
        }

        if (userEntity.getId() == null || userEntity.getName() == null || userEntity.getEmail() == null) {
            return false;
        }

        if (!EMAIL_PATTERN.matcher(userEntity.getEmail()).matches()) {
            return false;
        }

        if (userEntity.getHabits() == null) {
            return false;
        }

        for (HabitEntity habit : userEntity.getHabits()) {
            if (!isValidHabit(habit)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidHabit(HabitEntity habit) {
        if (habit.getName() == null || habit.getDescription() == null ||
                habit.getBadge() == null || !isValidBadge(habit.getBadge())) {
            return false;
        }

        if (habit.getFormationStage() == null) {
            return false;
        }

        return habit.getTrackings() != null;
    }

    private boolean isValidBadge(Badge badge) {
        try {
            Badge.valueOf(badge.name());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
