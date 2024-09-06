package com.example.demo.domain.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ProcessorBatchContextManager implements BatchContext {

    private ThreadLocal<Map<String, Integer>> context = ThreadLocal.withInitial(HashMap::new);

    @Override
    public void incrementUserCnt() {
        context.get().merge("userCnt", 1, Integer::sum);
    }

    @Override
    public void plusHabitCnt(int cnt) {
        context.get().merge("habitCnt", cnt, Integer::sum);
    }

    @Override
    public int getUserCnt() {
        return context.get().getOrDefault("userCnt", 0);
    }

    @Override
    public int getHabitCnt() {
        return context.get().getOrDefault("habitCnt", 0);
    }

    @Override
    public void clear() {
        context.remove();
    }
}
