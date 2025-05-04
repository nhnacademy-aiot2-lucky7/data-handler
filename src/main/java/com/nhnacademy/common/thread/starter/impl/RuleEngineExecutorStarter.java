package com.nhnacademy.common.thread.starter.impl;

import com.nhnacademy.common.thread.queue.impl.RuleEngineQueue;
import com.nhnacademy.common.thread.runnable.Task;
import com.nhnacademy.common.thread.starter.ExecutorStarter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public final class RuleEngineExecutorStarter extends ExecutorStarter {

    private final RuleEngineQueue ruleEngineQueue;

    private final AtomicBoolean running;

    public RuleEngineExecutorStarter(
            @Qualifier("ruleEngineExecutor") ExecutorService ruleEngineExecutor,
            @Qualifier("ruleEngineTaskRunning") AtomicBoolean running,
            RuleEngineQueue ruleEngineQueue
    ) {
        super(ruleEngineExecutor, running);
        this.ruleEngineQueue = ruleEngineQueue;
        this.running = running;
    }

    @Override
    protected Runnable createTask() {
        return new Task(ruleEngineQueue, running);
    }
}
