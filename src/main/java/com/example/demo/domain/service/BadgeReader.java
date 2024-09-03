package com.example.demo.domain.service;

import com.example.demo.infrastructure.entity.HabitEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;

public class BadgeReader implements ItemReader<HabitEntity> {

    private final JpaPagingItemReader<HabitEntity> delegate;

    public BadgeReader(EntityManagerFactory entityManagerFactory) {
        this.delegate = new JpaPagingItemReaderBuilder<HabitEntity>()
                .name("badgeReader")
                .queryString("SELECT h FROM Habit h")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(10)
                .build();
    }

    @Override
    public HabitEntity read() throws Exception {
        return delegate.read();
    }
}
