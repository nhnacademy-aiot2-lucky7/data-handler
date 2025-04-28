package com.nhnacademy.common.thread.shutdown;

import com.nhnacademy.common.thread.pool.ThreadPoolConfig;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public final class InfluxDBExecutorShutdown {

    private final ExecutorService influxDBExecutor;

    /**
     * @param influxDBExecutor {@link ThreadPoolConfig#influxDBExecutor()}
     */
    public InfluxDBExecutorShutdown(@Qualifier("influxDBExecutor") ExecutorService influxDBExecutor) {
        this.influxDBExecutor = influxDBExecutor;
    }

    @PreDestroy
    public void shutdown() {
        log.info("influxDBExecutor: shutting down...");
        influxDBExecutor.shutdown();
        try {
            if (!influxDBExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                influxDBExecutor.shutdownNow();
                log.warn("influxDBExecutor: did not terminate. forcing shutdown...");
            } else {
                log.info("influxDBExecutor: shutdown successfully.");
            }
        } catch (InterruptedException e) {
            log.error("influxDBExecutor: shutdown interrupted - {}", e.getMessage(), e);
            influxDBExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
