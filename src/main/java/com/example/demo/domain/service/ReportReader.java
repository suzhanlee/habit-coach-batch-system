package com.example.demo.domain.service;

import com.example.demo.infrastructure.entity.UserEntity;
import jakarta.persistence.EntityManagerFactory;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class ReportReader implements ItemStreamReader<UserEntity>, InitializingBean {

    private final EntityManagerFactory entityManagerFactory;
    private final Long reportTime;
    private final Validator<UserEntity> validator;
    private JpaPagingItemReader<UserEntity> delegate;

    public ReportReader(EntityManagerFactory entityManagerFactory, Long reportTime,
                        Validator<UserEntity> validator) {
        this.entityManagerFactory = entityManagerFactory;
        this.reportTime = reportTime;
        this.validator = validator;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(entityManagerFactory, "EntityManagerFactory must not be null");
        Assert.notNull(reportTime, "ReportTime must not be null");
        Assert.notNull(validator, "Validator must not be null");

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
        UserEntity userEntity = delegate.read();
        if (userEntity != null && !validator.isValid(userEntity)) {
            throw new IllegalStateException("validate error : user entity 의 형식이 올바르지 않습니다.");
        }
        return userEntity;
    }

    @Override
    public void open(org.springframework.batch.item.ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(org.springframework.batch.item.ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }
}
