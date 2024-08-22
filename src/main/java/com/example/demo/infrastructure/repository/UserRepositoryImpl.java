package com.example.demo.infrastructure.repository;

import com.example.demo.domain.model.User;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.infrastructure.entity.QHabitEntity;
import com.example.demo.infrastructure.entity.QHabitFormationStageEntity;
import com.example.demo.infrastructure.entity.QHabitTrackingEntity;
import com.example.demo.infrastructure.entity.QUserEntity;
import com.example.demo.infrastructure.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository, UserRepositoryCustom {

    private final UserJpaRepository userJpaRepository;
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository, JPAQueryFactory queryFactory) {
        this.userJpaRepository = userJpaRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<UserEntity> findByIdAndHabitsTrackingsCompletedDateBetween(Long userId, LocalDate startDate,
                                                                               LocalDate endDate) {
        QUserEntity userEntity = QUserEntity.userEntity;
        QHabitEntity habitEntity = QHabitEntity.habitEntity;
        QHabitFormationStageEntity habitFormationStageEntity = QHabitFormationStageEntity.habitFormationStageEntity;
        QHabitTrackingEntity habitTrackingEntity = QHabitTrackingEntity.habitTrackingEntity;

        UserEntity result = queryFactory
                .selectFrom(userEntity)
                .leftJoin(userEntity.habits, habitEntity).fetchJoin()
                .leftJoin(habitEntity.formationStage, habitFormationStageEntity).fetchJoin()
                .leftJoin(habitEntity.trackings, habitTrackingEntity)
                .where(userEntity.id.eq(userId)
                        .and(habitTrackingEntity.completedDate.between(startDate, endDate)
                                .or(habitTrackingEntity.isNull())))
                .distinct()
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public User findById(Long id) {
        return userJpaRepository.findById(id).orElseThrow().toUser();
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream().map(UserEntity::toUser).toList();
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.fromUser(user)).toUser();
    }

    @Override
    public void delete(User user) {
        userJpaRepository.delete(UserEntity.fromUser(user));
    }

    @Override
    public List<User> findByName(String name) {
        return userJpaRepository.findByName(name).stream().map(UserEntity::toUser).toList();
    }
}
