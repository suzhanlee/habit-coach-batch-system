package com.example.demo.repository;

import com.example.demo.domain.Habit;
import com.example.demo.domain.repository.HabitRepository;
import com.example.demo.entity.HabitEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HabitRepositoryImpl implements HabitRepository {

    private final HabitJpaRepository habitJpaRepository;

    @Override
    public List<Habit> findHabitList(Long userId) {
        return habitJpaRepository.findAllByUserId(userId).stream().map(HabitEntity::toHabit).toList();
    }
}
