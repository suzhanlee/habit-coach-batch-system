package com.example.demo.domain.service;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
public class ReportChunkListener implements ChunkListener {

    private final ReaderBatchContextManager readerContext;
    private final ProcessorBatchContextManager processContext;

    public ReportChunkListener(ReaderBatchContextManager readerContext, ProcessorBatchContextManager processContext) {
        this.readerContext = readerContext;
        this.processContext = processContext;
    }

    @Override
    public void afterChunk(ChunkContext context) {
        if (readerContext.getUserCnt() != processContext.getUserCnt()
                || readerContext.getHabitCnt() != processContext.getHabitCnt()) {
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
