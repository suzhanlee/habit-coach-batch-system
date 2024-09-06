package com.example.demo.domain.service;

import com.example.demo.infrastructure.entity.HabitEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class BadgeReader implements ItemStreamReader<HabitEntity>, InitializingBean {

    private final EntityManagerFactory entityManagerFactory;
    private JpaPagingItemReader<HabitEntity> delegate;

    public BadgeReader(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(entityManagerFactory, "EntityManagerFactory must not be null");

        this.delegate = new JpaPagingItemReaderBuilder<HabitEntity>()
                .name("badgeReader")
                .queryString("SELECT h FROM HabitEntity h")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(10)
                .build();
    }

    @Override
    public HabitEntity read() throws Exception {
        return delegate.read();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }
}
