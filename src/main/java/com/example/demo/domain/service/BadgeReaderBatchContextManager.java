package com.example.demo.domain.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class BadgeReaderBatchContextManager implements BadgeBatchContext {

    private ThreadLocal<Map<String, Integer>> context = ThreadLocal.withInitial(HashMap::new);

    @Override
    public void incrementHabitCnt() {
        context.get().merge("habitCnt", 1, Integer::sum);
    }

    @Override
    public void plusTrackingsCnt(int cnt) {
        context.get().merge("trackingsCnt", cnt, Integer::sum);
    }

    @Override
    public int getHabitCnt() {
        return context.get().getOrDefault("habitCnt", 0);
    }

    @Override
    public int getTrackingsCnt() {
        return context.get().getOrDefault("trackingsCnt", 0);
    }

    @Override
    public void clear() {
        context.remove();
    }
}
