package com.example.demo.domain.repository;

import com.example.demo.domain.User;
import java.util.List;

public interface UserRepository {

    User findById(Long id);

    List<User> findAll();

    User save(User user);

    void delete(User user);

    List<User> findByName(String name);
}
