package com.example.demo.repository;

import com.example.demo.domain.User;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.entity.UserEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

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
