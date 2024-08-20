package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import java.time.LocalDate;
import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<UserEntity> findUserWithHabitsAndTrackingsForMonth(Long userId, LocalDate startDate, LocalDate endDate);
}
