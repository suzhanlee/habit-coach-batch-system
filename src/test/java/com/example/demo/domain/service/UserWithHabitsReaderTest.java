package com.example.demo.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.infrastructure.entity.HabitEntity;
import com.example.demo.infrastructure.entity.HabitFormationStageEntity;
import com.example.demo.infrastructure.entity.HabitTrackingEntity;
import com.example.demo.infrastructure.entity.UserEntity;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class UserWithHabitsReaderTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private UserWithHabitsReader reader;

    @BeforeEach
    void setUp() {
        UserEntity userEntity = new UserEntity("suchan", "suchan@example.com");
        HabitEntity habitEntity = new HabitEntity("exercise", "Description for exercise");
        HabitFormationStageEntity formationStageEntity = new HabitFormationStageEntity(2,
                Arrays.asList("Question1", "Question2"), Arrays.asList("Answer1", "Answer2"), "feedback");
        HabitTrackingEntity trackingEntity = new HabitTrackingEntity(habitEntity, LocalDate.of(2024, 8, 15));

        formationStageEntity.addHabitEntity(habitEntity);
        trackingEntity.addHabitEntity(habitEntity);
        habitEntity.addUserEntity(userEntity);

        testEntityManager.persist(userEntity);
        testEntityManager.flush();

        Long reportTime = LocalDate.of(2024, 8, 22).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        reader = new UserWithHabitsReader(entityManagerFactory, reportTime);
    }

    @Test
    @DisplayName("UserWithHabitsReader가 올바른 사용자와 관련 데이터를 읽어온다")
    void read_chunk() throws Exception {
        // given && when
        List<UserEntity> loadedUsers = new ArrayList<>();

        UserEntity user;
        while ((user = reader.read()) != null) {
            loadedUsers.add(user);
        }

        // then
        assertEquals(1, loadedUsers.size());

        UserEntity loadedUser = loadedUsers.get(0);
        assertEquals("suchan", loadedUser.getName());
        assertEquals("suchan@example.com", loadedUser.getEmail());

        assertFalse(loadedUser.getHabits().isEmpty());
        HabitEntity habit = loadedUser.getHabits().get(0);
        assertEquals("exercise", habit.getName());
        assertEquals("Description for exercise", habit.getDescription());

        assertNotNull(habit.getFormationStage());
        HabitFormationStageEntity formationStage = habit.getFormationStage();
        assertEquals(2, formationStage.getStage());
        assertEquals(2, formationStage.getQuestions().size());
        assertEquals(2, formationStage.getAnswers().size());
        assertEquals("feedback", formationStage.getFeedback());

        assertFalse(habit.getTrackings().isEmpty());
        HabitTrackingEntity tracking = habit.getTrackings().get(0);
        assertEquals(LocalDate.of(2024, 8, 15), tracking.getCompletedDate());
    }
}
