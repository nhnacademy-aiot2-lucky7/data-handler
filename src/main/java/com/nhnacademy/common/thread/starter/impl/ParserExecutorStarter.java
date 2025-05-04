package com.nhnacademy.common.thread.starter.impl;

import com.nhnacademy.common.thread.queue.impl.ParserQueue;
import com.nhnacademy.common.thread.runnable.Task;
import com.nhnacademy.common.thread.starter.ExecutorStarter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public final class ParserExecutorStarter extends ExecutorStarter {

    private final ParserQueue parserQueue;

    private final AtomicBoolean running;

    public ParserExecutorStarter(
            @Qualifier("parserExecutor") ExecutorService parserExecutor,
            @Qualifier("parserTaskRunning") AtomicBoolean running,
            ParserQueue parserQueue
    ) {
        super(parserExecutor, running);
        this.parserQueue = parserQueue;
        this.running = running;
    }

    @Override
    protected Runnable createTask() {
        return new Task(parserQueue, running);
    }
}
