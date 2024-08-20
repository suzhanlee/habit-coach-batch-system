package com.example.demo.domain.repository;

import com.example.demo.domain.Habit;
import java.util.List;

public interface HabitRepository {

    List<Habit> findHabitList(Long userId);
}
