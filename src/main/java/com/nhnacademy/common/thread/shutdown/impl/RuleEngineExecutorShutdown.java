package com.nhnacademy.common.thread.shutdown.impl;

import com.nhnacademy.common.config.ThreadPoolConfig;
import com.nhnacademy.common.thread.shutdown.ExecutorShutdown;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public final class RuleEngineExecutorShutdown extends ExecutorShutdown {

    private final ExecutorService ruleEngineExecutor;

    /**
     * @param ruleEngineExecutor {@link ThreadPoolConfig#ruleEngineExecutor()}
     */
    public RuleEngineExecutorShutdown(
            @Qualifier("ruleEngineExecutor") ExecutorService ruleEngineExecutor
    ) {
        this.ruleEngineExecutor = ruleEngineExecutor;
    }

    public void shutdown() {
        shutdownExecutor(ruleEngineExecutor, "ruleEngineExecutor");
    }
}
