package com.example.demo.infrastructure.entity;

import com.example.demo.domain.model.Habit;
import com.example.demo.domain.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HabitEntity> habits = new ArrayList<>();

    public UserEntity(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static UserEntity fromUser(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.id = user.getId();
        userEntity.name = user.getName();
        userEntity.email = user.getEmail();

        for (Habit habit : user.getHabits()) {
            HabitEntity habitEntity = HabitEntity.fromHabit(habit);
            userEntity.addHabitEntity(habitEntity);
        }

        return userEntity;
    }

    public User toUser() {
        List<Habit> habitList = this.habits.stream()
                .map(HabitEntity::toHabit)
                .toList();
        return new User(id, name, email, habitList);
    }

    public void addHabitEntity(HabitEntity habitEntity) {
        this.habits.add(habitEntity);
        habitEntity.addUserEntity(this);
    }
}
