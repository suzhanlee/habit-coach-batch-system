package com.example.demo.infrastructure.repository;

import com.example.demo.infrastructure.entity.HabitTrackingEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitTrackingJpaRepository extends JpaRepository<HabitTrackingEntity, Long> {

    List<HabitTrackingEntity> findAllByHabitIdAndCompletedDateBetween(Long habitId,
                                                                      LocalDate startDate,
                                                                      LocalDate endDate);

    long countByHabitIdAndCompletedDateBetween(Long habitId, LocalDate startDate, LocalDate endDate);
}
