package com.example.demo.domain.service;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
public class BadgeChunkListener implements ChunkListener {

    private final BadgeReaderBatchContextManager readerContext;
    private final BadgeProcessorBatchContextManager processContext;

    public BadgeChunkListener(BadgeReaderBatchContextManager readerContext, BadgeProcessorBatchContextManager processContext) {
        this.readerContext = readerContext;
        this.processContext = processContext;
    }

    @Override
    public void afterChunk(ChunkContext context) {
        if (readerContext.getHabitCnt() != processContext.getHabitCnt()
                || readerContext.getTrackingsCnt() != processContext.getTrackingsCnt()) {
            throw new IllegalStateException("chunk error : chunk check sum exception ");
        }
        readerContext.clear();
        processContext.clear();
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        readerContext.clear();
        processContext.clear();
    }
}
