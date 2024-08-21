package com.example.demo.domain.service;

import com.example.demo.infrastructure.entity.UserEntity;
import jakarta.persistence.EntityManagerFactory;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;

@Slf4j
public class UserWithHabitsReader implements ItemReader<UserEntity> {

    private final JpaPagingItemReader<UserEntity> delegate;

    public UserWithHabitsReader(EntityManagerFactory entityManagerFactory, Long reportTime) {
        this.delegate = new JpaPagingItemReaderBuilder<UserEntity>()
                .name("userWithHabitsReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(
                        "SELECT DISTINCT u FROM UserEntity u " +
                                "LEFT JOIN FETCH u.habits h " +
                                "LEFT JOIN FETCH h.formationStage " +
                                "LEFT JOIN h.trackings t " +
                                "WHERE t.completedDate BETWEEN :startDate AND :endDate " +
                                "OR t.completedDate IS NULL")
                .parameterValues(getParameterValues(reportTime))
                .pageSize(10)
                .build();
    }

    private Map<String, Object> getParameterValues(Long reportTime) {
        LocalDate startDate = LocalDate.ofInstant(Instant.ofEpochMilli(reportTime), ZoneId.systemDefault())
                .withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        return Map.of("startDate", startDate, "endDate", endDate);
    }

    @Override
    public UserEntity read() throws Exception {
        return delegate.read();
    }
}
