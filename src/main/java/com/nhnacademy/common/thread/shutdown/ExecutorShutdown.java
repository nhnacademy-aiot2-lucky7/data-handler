package com.nhnacademy.common.thread.shutdown;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class ExecutorShutdown {

    protected void shutdownExecutor(ExecutorService executor, String name) {
        log.info("{}: shutting down...", name);
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                log.warn("{}: did not terminate. forcing shutdown...", name);
            } else {
                log.info("{}: shutdown successfully.", name);
            }
        } catch (InterruptedException e) {
            log.error("{}: shutdown interrupted - {}", name, e.getMessage(), e);
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
