package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long>, UserRepositoryCustom {

    List<UserEntity> findByName(String name);
}
