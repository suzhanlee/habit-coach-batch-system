package com.example.demo.domain.repository;

import com.example.demo.domain.model.Habit;
import java.util.List;

public interface HabitRepository {

    List<Habit> findHabitList(Long userId);

    void saveAll(List<Habit> habits);
}
