package com.example.demo.domain.service;

public interface BadgeBatchContext {

    void incrementHabitCnt();

    void plusTrackingsCnt(int cnt);

    int getHabitCnt();

    int getTrackingsCnt();

    void clear();
}
