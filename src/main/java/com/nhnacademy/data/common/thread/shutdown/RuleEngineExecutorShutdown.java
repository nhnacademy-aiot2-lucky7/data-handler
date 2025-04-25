package com.nhnacademy.data.common.thread.shutdown;

import com.nhnacademy.data.common.thread.pool.ThreadPoolConfig;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public final class RuleEngineExecutorShutdown {

    private final ExecutorService ruleEngineExecutor;

    /**
     * @param ruleEngineExecutor {@link ThreadPoolConfig#ruleEngineExecutor()}
     */
    public RuleEngineExecutorShutdown(@Qualifier("ruleEngineExecutor") ExecutorService ruleEngineExecutor) {
        this.ruleEngineExecutor = ruleEngineExecutor;
    }

    @PreDestroy
    public void shutdown() {
        log.info("ruleEngineExecutor: shutting down...");
        ruleEngineExecutor.shutdown();
        try {
            if (!ruleEngineExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                ruleEngineExecutor.shutdownNow();
                log.warn("ruleEngineExecutor: did not terminate. forcing shutdown...");
            } else {
                log.info("ruleEngineExecutor: shutdown successfully.");
            }
        } catch (InterruptedException e) {
            log.error("ruleEngineExecutor: shutdown interrupted - {}", e.getMessage(), e);
            ruleEngineExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
