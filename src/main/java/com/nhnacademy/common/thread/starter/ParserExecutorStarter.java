package com.nhnacademy.common.thread.starter;

import com.nhnacademy.common.thread.queue.ParserQueue;
import com.nhnacademy.common.thread.runnable.ParserTask;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public final class ParserExecutorStarter {

    private final ExecutorService parserExecutor;

    private final ParserQueue parserQueue;

    private final AtomicBoolean running;

    public ParserExecutorStarter(
            @Qualifier("parserExecutor") ExecutorService parserExecutor,
            @Qualifier("parserTaskRunning") AtomicBoolean running,
            ParserQueue parserQueue
    ) {
        this.parserExecutor = parserExecutor;
        this.parserQueue = parserQueue;
        this.running = running;
    }

    @PostConstruct
    private void start() {
        parserExecutor.submit(
                new ParserTask(parserQueue, running)
        );
    }
}
