package com.nhnacademy.common.thread.runnable;

import com.nhnacademy.common.thread.Executable;
import com.nhnacademy.common.thread.queue.ParserQueue;

/**
 * Parser 전용 작업 Queue({@link ParserQueue})에서 작업을 꺼내 <br>
 * 실행하는 {@link Runnable} 구현 클래스입니다.
 * <hr>
 * 이 클래스는 Parser 전용 Thread Pool에서 주기적으로 실행되며, <br>
 * Queue에 쌓인 작업을 하나씩 가져와 처리합니다.
 *
 * @see com.nhnacademy.common.thread.pool.ThreadPoolConfig
 */
public final class ConsumeParserQueue implements Runnable {

    /**
     * Parser 작업 대기열
     */
    private final ParserQueue parserQueue;

    public ConsumeParserQueue(ParserQueue parserQueue) {
        this.parserQueue = parserQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Executable job = parserQueue.take();
                job.execute();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
