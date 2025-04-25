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
public final class ParserExecutorShutdown {

    private final ExecutorService parserExecutor;

    /**
     * @param parserExecutor {@link ThreadPoolConfig#parserExecutor()}
     */
    public ParserExecutorShutdown(@Qualifier("parserExecutor") ExecutorService parserExecutor) {
        this.parserExecutor = parserExecutor;
    }

    @PreDestroy
    public void shutdown() {
        log.info("parserExecutor: shutting down...");
        parserExecutor.shutdown();
        try {
            if (!parserExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                parserExecutor.shutdownNow();
                log.warn("parserExecutor: did not terminate. forcing shutdown...");
            } else {
                log.info("parserExecutor: shutdown successfully.");
            }
        } catch (InterruptedException e) {
            log.error("parserExecutor: shutdown interrupted - {}", e.getMessage(), e);
            parserExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
