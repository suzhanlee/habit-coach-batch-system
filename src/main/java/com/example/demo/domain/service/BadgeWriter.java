package com.example.demo.domain.service;

import com.example.demo.domain.model.Habit;
import com.example.demo.domain.repository.HabitRepository;
import java.util.List;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BadgeWriter implements ItemWriter<Habit> {

    private final HabitRepository habitRepository;

    public BadgeWriter(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    @Override
    public void write(Chunk<? extends Habit> chunk) throws Exception {
        habitRepository.saveAll((List<Habit>) chunk.getItems());
    }
}
