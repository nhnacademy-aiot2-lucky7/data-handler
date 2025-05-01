package com.nhnacademy.common.thread.starter;

import com.nhnacademy.common.properties.RuleEngineProperties;
import com.nhnacademy.common.thread.queue.RuleEngineQueue;
import com.nhnacademy.common.thread.runnable.RuleEngineTask;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public final class RuleEngineExecutorStarter {

    private final ExecutorService ruleEngineExecutor;

    private final RuleEngineQueue ruleEngineQueue;

    private final RuleEngineProperties properties;

    private final AtomicBoolean running;

    public RuleEngineExecutorStarter(
            @Qualifier("ruleEngineExecutor") ExecutorService ruleEngineExecutor,
            @Qualifier("ruleEngineTaskRunning") AtomicBoolean running,
            RuleEngineProperties properties, RuleEngineQueue ruleEngineQueue
    ) {
        this.ruleEngineExecutor = ruleEngineExecutor;
        this.ruleEngineQueue = ruleEngineQueue;
        this.properties = properties;
        this.running = running;
    }

    @PostConstruct
    private void start() {
        if (!properties.isStart()) return;
        ruleEngineExecutor.submit(
                new RuleEngineTask(ruleEngineQueue, running)
        );
    }
}
