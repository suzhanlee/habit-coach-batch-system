package com.example.demo.infrastructure.repository;

import com.example.demo.infrastructure.entity.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long>, UserRepositoryCustom {

    List<UserEntity> findByName(String name);
}
