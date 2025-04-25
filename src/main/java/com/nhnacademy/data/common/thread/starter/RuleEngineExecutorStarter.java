package com.nhnacademy.data.common.thread.starter;

import com.nhnacademy.data.common.thread.queue.RuleEngineQueue;
import com.nhnacademy.data.common.thread.runnable.ConsumeRuleEngineQueue;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
public final class RuleEngineExecutorStarter {

    private final ExecutorService ruleEngineExecutor;

    private final RuleEngineQueue ruleEngineQueue;

    public RuleEngineExecutorStarter(
            @Qualifier("ruleEngineExecutor") ExecutorService ruleEngineExecutor,
            RuleEngineQueue ruleEngineQueue
    ) {
        this.ruleEngineExecutor = ruleEngineExecutor;
        this.ruleEngineQueue = ruleEngineQueue;
    }

    @PostConstruct
    private void start() {
        ruleEngineExecutor.submit(
                new ConsumeRuleEngineQueue(ruleEngineQueue)
        );
    }
}
