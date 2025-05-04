package com.nhnacademy.common.thread.runnable.impl;

import com.nhnacademy.common.config.ThreadPoolConfig;
import com.nhnacademy.common.thread.Executable;
import com.nhnacademy.common.thread.queue.impl.RuleEngineQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Rule Engine 전용 작업 Queue({@link RuleEngineQueue})에서 작업을 꺼내 <br>
 * 실행하는 {@link Runnable} 구현 클래스입니다.
 * <hr>
 * 이 클래스는 Rule Engine 전용 Thread Pool에서 주기적으로 실행되며, <br>
 * Queue에 쌓인 작업을 하나씩 가져와 처리합니다.
 *
 * @see ThreadPoolConfig
 */
@Deprecated
@Slf4j
public final class RuleEngineTask implements Runnable {

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
                Executable ruleEngine = ruleEngineQueue.take();
                ruleEngine.execute();
            } catch (InterruptedException e) {
                log.error("Thread interrupted: {}", e.getMessage());
                ruleEngineThread.interrupt();
            } catch (Throwable e) {
                log.warn("{}", e.getMessage(), e);
            }
        }
    }
}
