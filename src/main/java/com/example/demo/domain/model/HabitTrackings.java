package com.example.demo.domain.model;

import java.util.List;

public record HabitTrackings(List<HabitTracking> habitTrackings) {

    public int countMaxStreak() {
        List<HabitTracking> sortedDescTracking = createSortTrackingDesc();
        if (hasNeverPerformedHabit(sortedDescTracking)) {
            return 0;
        }
        return countContinuousStreak(sortedDescTracking);
    }

    public List<HabitTracking> createSortTrackingDesc() {
        return this.habitTrackings.stream()
                .sorted((t1, t2) -> t2.compareCompletedDate(t1))
                .toList();
    }

    private boolean hasNeverPerformedHabit(List<HabitTracking> sortedDescTracking) {
        return sortedDescTracking.isEmpty();
    }

    private int countContinuousStreak(List<HabitTracking> sortedDescTracking) {
        int maxStreak = 1;
        int currentContinuousStreak = 1;

        for (int i = 1; i < sortedDescTracking.size(); i++) {
            HabitTracking previousTracking = sortedDescTracking.get(i - 1);
            HabitTracking currentTracking = sortedDescTracking.get(i);
            currentContinuousStreak = updateCurrentStreak(previousTracking, currentTracking, currentContinuousStreak);
            maxStreak = Math.max(maxStreak, currentContinuousStreak);
        }

        return maxStreak;
    }

    private int updateCurrentStreak(HabitTracking previousTracking, HabitTracking currentTracking, int currentStreak) {
        if (previousTracking.isContinuousDate(currentTracking)) {
            return currentStreak + 1;
        }
        return 1;
    }
}
