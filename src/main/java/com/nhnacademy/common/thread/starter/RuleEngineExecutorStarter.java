package com.nhnacademy.common.thread.starter;

import com.nhnacademy.common.thread.queue.RuleEngineQueue;
import com.nhnacademy.common.thread.runnable.ConsumeRuleEngineQueue;
import com.nhnacademy.rule.RuleEngine;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
public final class RuleEngineExecutorStarter {

    private final ExecutorService ruleEngineExecutor;

    private final RuleEngineQueue ruleEngineQueue;

    private final RuleEngine ruleEngine;

    public RuleEngineExecutorStarter(
            @Qualifier("ruleEngineExecutor") ExecutorService ruleEngineExecutor,
            RuleEngineQueue ruleEngineQueue, RuleEngine ruleEngine
    ) {
        this.ruleEngineExecutor = ruleEngineExecutor;
        this.ruleEngineQueue = ruleEngineQueue;
        this.ruleEngine = ruleEngine;
    }

    @PostConstruct
    private void start() {
        ruleEngineExecutor.submit(
                new ConsumeRuleEngineQueue(ruleEngineQueue, ruleEngine)
        );
    }
}
