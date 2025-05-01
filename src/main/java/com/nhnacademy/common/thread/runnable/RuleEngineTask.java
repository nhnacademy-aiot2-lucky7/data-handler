package com.nhnacademy.common.thread.runnable;

import com.nhnacademy.common.thread.Executable;
import com.nhnacademy.common.thread.log.RetryLogger;
import com.nhnacademy.common.thread.queue.RuleEngineQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Rule Engine 전용 작업 Queue({@link RuleEngineQueue})에서 작업을 꺼내 <br>
 * 실행하는 {@link Runnable} 구현 클래스입니다.
 * <hr>
 * 이 클래스는 Rule Engine 전용 Thread Pool에서 주기적으로 실행되며, <br>
 * Queue에 쌓인 작업을 하나씩 가져와 처리합니다.
 *
 * @see com.nhnacademy.common.thread.pool.ThreadPoolConfig
 */
@Slf4j
public final class RuleEngineTask implements Runnable {

    private static final int MAX_RETRIES = 3;

    private static final long RETRY_DELAY_MS = 100L;

    private final String action = "deliver 'SensorData'";

    /**
     * Rule Engine 작업 대기열
     */
    private final RuleEngineQueue ruleEngineQueue;

    private final AtomicBoolean running;

    public RuleEngineTask(RuleEngineQueue ruleEngineQueue, AtomicBoolean running) {
        this.ruleEngineQueue = ruleEngineQueue;
        this.running = running;
    }

    @Override
    public void run() {
        Thread ruleEngineThread = Thread.currentThread();
        while (!ruleEngineThread.isInterrupted()
                && (running.get() || ruleEngineQueue.isNotEmpty())
        ) {
            try {
                execute(ruleEngineQueue.take());
            } catch (InterruptedException e) {
                log.error("Thread interrupted: {}", e.getMessage());
                ruleEngineThread.interrupt();
            }
        }
    }

    private void execute(Executable ruleEngine) throws InterruptedException {
        try {
            ruleEngine.execute();
        } catch (Throwable e) {
            int retryCount = ruleEngine.getRetryCount();
            if (retryCount < MAX_RETRIES) {
                ruleEngine.incrementRetryCount();
                RetryLogger.logRetry(action, retryCount + 1, MAX_RETRIES, e);

                Thread.sleep(RETRY_DELAY_MS);
                ruleEngineQueue.put(ruleEngine);
            } else {
                RetryLogger.logFailure(action, MAX_RETRIES, e);
            }
        }
    }
}
