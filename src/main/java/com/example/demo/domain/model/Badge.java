package com.example.demo.domain.model;

import java.util.Arrays;

public enum Badge {

    CHALLENGER(300),
    DIAMOND(100),
    PLATINUM(66),
    GOLD(30),
    SILVER(10),
    BRONZE(5),
    UN_RANK(0);

    private final long streakCount;

    Badge(int streakCount) {
        this.streakCount = streakCount;
    }

    public static Badge findBadgeByStreak(int streakCount) {
        return Arrays.stream(values())
                .sorted((b1, b2) -> Long.compare(b2.streakCount, b1.streakCount))
                .filter(b -> streakCount >= b.streakCount)
                .findFirst()
                .orElse(UN_RANK);
    }
}
