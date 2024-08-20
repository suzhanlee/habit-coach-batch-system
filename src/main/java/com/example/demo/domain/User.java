package com.example.demo.domain;

import java.util.List;
import lombok.Getter;

@Getter
public class User {

    private Long id;
    private String name;
    private String email;
    private List<Habit> habits;

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
