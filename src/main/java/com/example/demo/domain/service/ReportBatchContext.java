package com.example.demo.domain.service;

public interface ReportBatchContext {

    void incrementUserCnt();

    void plusHabitCnt(int cnt);

    int getUserCnt();

    int getHabitCnt();

    void clear();
}
