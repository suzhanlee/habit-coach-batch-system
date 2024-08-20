package com.example.demo.repository;

import com.example.demo.domain.Habit;
import com.example.demo.domain.HabitTracking;
import com.example.demo.domain.repository.HabitTrackingRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HabitTrackingRepositoryImpl implements HabitTrackingRepository {

    private final HabitTrackingJpaRepository habitTrackingJpaRepository;
    private final HabitJpaRepository habitJpaRepository;

    @Override
    public List<HabitTracking> findHabitTrackingList(Long habitId, LocalDate startDate, LocalDate endDate) {
        Habit habit = habitJpaRepository.findById(habitId).orElseThrow().toHabit();

        return habitTrackingJpaRepository.findAllByHabitIdAndCompletedDateBetween(habitId, startDate, endDate)
                .stream()
                .map(entity -> entity.toHabitTracking(habit))
                .toList();
    }

    @Override
    public long countHabitTrackingInDateRange(Long habitId, LocalDate startDate, LocalDate endDate) {
        return habitTrackingJpaRepository.countHabitTrackingInDateRange(habitId, startDate, endDate);
    }
}
