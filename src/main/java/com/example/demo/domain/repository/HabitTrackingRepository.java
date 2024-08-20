package com.example.demo.domain.repository;

import com.example.demo.domain.HabitTracking;
import java.time.LocalDate;
import java.util.List;

public interface HabitTrackingRepository {

    List<HabitTracking> findHabitTrackingList(Long habitId, LocalDate startDate, LocalDate endDate);

    long countHabitTrackingInDateRange(Long habitId, LocalDate startDate, LocalDate endDate);
}
