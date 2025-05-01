package com.nhnacademy.common.thread.starter;

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

    private final AtomicBoolean running;

    public RuleEngineExecutorStarter(
            @Qualifier("ruleEngineExecutor") ExecutorService ruleEngineExecutor,
            @Qualifier("ruleEngineTaskRunning") AtomicBoolean running,
            RuleEngineQueue ruleEngineQueue
    ) {
        this.ruleEngineExecutor = ruleEngineExecutor;
        this.ruleEngineQueue = ruleEngineQueue;
        this.running = running;
    }

    /// TODO: 프로퍼티스에 아래의 작업을 수행하지 않는 옵션 추가 예정
    @PostConstruct
    private void start() {
        /*ruleEngineExecutor.submit(
                new RuleEngineTask(ruleEngineQueue, running)
        );*/
    }
}
