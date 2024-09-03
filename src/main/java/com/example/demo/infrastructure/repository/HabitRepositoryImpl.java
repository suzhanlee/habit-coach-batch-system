package com.example.demo.infrastructure.repository;

import com.example.demo.domain.model.Habit;
import com.example.demo.domain.repository.HabitRepository;
import com.example.demo.infrastructure.entity.HabitEntity;
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

    @Override
    public void saveAll(List<Habit> habits) {
        List<HabitEntity> habitEntities = habits.stream().map(HabitEntity::fromHabit).toList();
        habitJpaRepository.saveAll(habitEntities);
    }
}
