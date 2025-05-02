package com.nhnacademy.common.thread.shutdown.impl;

import com.nhnacademy.common.thread.pool.ThreadPoolConfig;
import com.nhnacademy.common.thread.shutdown.AbstractExecutorShutdown;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public final class RuleEngineExecutorShutdown extends AbstractExecutorShutdown {

    private final ExecutorService ruleEngineExecutor;

    /**
     * @param ruleEngineExecutor {@link ThreadPoolConfig#ruleEngineExecutor()}
     */
    public RuleEngineExecutorShutdown(
            @Qualifier("ruleEngineExecutor") ExecutorService ruleEngineExecutor
    ) {
        this.ruleEngineExecutor = ruleEngineExecutor;
    }

    @PreDestroy
    public void shutdown() {
        shutdownExecutor(ruleEngineExecutor, "ruleEngineExecutor");
    }
}
