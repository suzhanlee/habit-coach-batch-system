package com.example.demo.infrastructure.repository;

import com.example.demo.infrastructure.entity.UserEntity;
import java.time.LocalDate;
import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<UserEntity> findUserWithHabitsAndTrackingsForMonth(Long userId, LocalDate startDate, LocalDate endDate);
}
