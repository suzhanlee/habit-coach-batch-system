package com.example.demo.infrastructure.repository;

import com.example.demo.infrastructure.entity.HabitEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HabitJpaRepository extends JpaRepository<HabitEntity, Long> {

    @Query("SELECT h FROM HabitEntity h JOIN FETCH h.user WHERE h.user.id = :userId")
    List<HabitEntity> findAllByUserId(@Param("userId") Long userId);
}
