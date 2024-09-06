package com.example.demo.domain.service;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
public class ReportChunkListener implements ChunkListener {

    private final ReportReaderReportBatchContextManager readerContext;
    private final ReportProcessorReportBatchContextManager processContext;

    public ReportChunkListener(ReportReaderReportBatchContextManager readerContext, ReportProcessorReportBatchContextManager processContext) {
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
