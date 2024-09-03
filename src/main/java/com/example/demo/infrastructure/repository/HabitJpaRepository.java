package com.example.demo.infrastructure.repository;

import com.example.demo.infrastructure.entity.HabitEntity;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HabitJpaRepository extends JpaRepository<HabitEntity, Long> {

    @Query("SELECT DISTINCT h FROM HabitEntity h " +
            "JOIN FETCH h.user " +
            "LEFT JOIN FETCH h.formationStage " +
            "LEFT JOIN FETCH h.trackings " +
            "WHERE h.user.id = :userId")
    List<HabitEntity> findAllByUserId(@Param("userId") Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    <S extends HabitEntity> List<S> saveAll(Iterable<S> entities);
}
