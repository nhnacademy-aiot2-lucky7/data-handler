package com.nhnacademy.common.thread.runnable.impl;

import com.nhnacademy.common.config.ThreadPoolConfig;
import com.nhnacademy.common.thread.Executable;
import com.nhnacademy.common.thread.queue.impl.ParserQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Parser 전용 작업 Queue({@link ParserQueue})에서 작업을 꺼내 <br>
 * 실행하는 {@link Runnable} 구현 클래스입니다.
 * <hr>
 * 이 클래스는 Parser 전용 Thread Pool에서 주기적으로 실행되며, <br>
 * Queue에 쌓인 작업을 하나씩 가져와 처리합니다.
 *
 * @see ThreadPoolConfig
 */
@Deprecated
@Slf4j
public final class ParserTask implements Runnable {

    /**
     * Parser 작업 대기열
     */
    private final ParserQueue parserQueue;

    private final AtomicBoolean running;

    public ParserTask(ParserQueue parserQueue, AtomicBoolean running) {
        this.parserQueue = parserQueue;
        this.running = running;
    }

    @Override
    public void run() {
        Thread parserThread = Thread.currentThread();
        while (!parserThread.isInterrupted()
                && (running.get() || parserQueue.isNotEmpty())
        ) {
            try {
                Executable parser = parserQueue.take();
                parser.execute();
            } catch (InterruptedException e) {
                log.error("Thread interrupted: {}", e.getMessage());
                parserThread.interrupt();
            } catch (Throwable e) {
                log.warn("Failed to parsing: {}", e.getMessage(), e);
            }
        }
    }
}
