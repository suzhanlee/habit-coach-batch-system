package com.example.demo.repository;

import com.example.demo.entity.HabitTrackingEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitTrackingJpaRepository extends JpaRepository<HabitTrackingEntity, Long> {

    List<HabitTrackingEntity> findAllByHabitIdAndCompletedDateBetween(Long habitId,
                                                                      LocalDate startDate,
                                                                      LocalDate endDate);

    long countHabitTrackingInDateRange(Long habitId, LocalDate startDate, LocalDate endDate);
}
