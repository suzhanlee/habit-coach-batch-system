package com.example.demo.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.domain.model.Badge;
import com.example.demo.domain.model.Habit;
import com.example.demo.domain.model.HabitFormationStage;
import com.example.demo.domain.model.HabitTracking;
import com.example.demo.domain.model.User;
import com.example.demo.domain.repository.HabitRepository;
import com.example.demo.domain.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.Chunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BadgeWriterTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HabitRepository habitRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("업데이트 된 뱃지를 실제 db에 업데이트한다.")
    void write() throws Exception {
        // given
        List<Habit> habits = new ArrayList<>(List.of(
                new Habit("habit1", "desc1", new HabitFormationStage(1), createGivenTrackings(1, 6)),
                new Habit("habit2", "desc2", new HabitFormationStage(2), createGivenTrackings(7, 29)),
                new Habit("habit3", "desc3", new HabitFormationStage(3), createGivenTrackings(36, 100))
        ));

        User user = new User("suchan", "wlscww@kakao.com", habits);
        userRepository.save(user);


        for (Habit habit : user.getHabits()) {
            habit.updateBadgeLevel();
        }

        Chunk<Habit> givenChunk = new Chunk<>(habits);
        BadgeWriter badgeWriter = new BadgeWriter(habitRepository);

        // when
        badgeWriter.write(givenChunk);

        // then
        Long userId = user.getId();
        List<Habit> result = habitRepository.findHabitList(userId);
        List<Badge> badges = result.stream()
                .map(habit -> {
                    Badge badge = habit.getBadge();
                    assertThat(badge).isNotNull();
                    return badge;
                })
                .toList();

        assertThat(badges).containsExactlyInAnyOrder(Badge.BRONZE, Badge.SILVER, Badge.DIAMOND);
    }

    private List<HabitTracking> createGivenTrackings(int startNumber, int totalTrackingCount) {
        List<HabitTracking> trackings = new ArrayList<>();
        int lastNumber = startNumber + totalTrackingCount;
        for (int i = startNumber; i < lastNumber; i++) {
            HabitTracking habitTracking = new HabitTracking(LocalDate.of(2024, 1, 1).plusDays(i));
            trackings.add(habitTracking);
        }
        return trackings;
    }
}
