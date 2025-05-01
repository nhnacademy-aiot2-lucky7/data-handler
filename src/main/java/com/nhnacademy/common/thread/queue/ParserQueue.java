package com.nhnacademy.common.thread.queue;

import com.nhnacademy.common.thread.Executable;
import com.nhnacademy.common.thread.properties.ParserThreadPoolProperties;
import com.nhnacademy.common.thread.runnable.ParserTask;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Parser가 처리할 작업들을 저장하고, 순차적으로 꺼내어 <br>
 * 실행할 수 있도록 관리하는 대기열 클래스입니다.
 * <hr>
 *
 * @see ParserTask
 */
@Component
public final class ParserQueue {

    /**
     * Parser Queue
     */
    private final BlockingQueue<Executable> queue;

    public ParserQueue(ParserThreadPoolProperties properties) {
        this.queue = new LinkedBlockingQueue<>(properties.getCapacity());
    }

    /**
     * Parser가 처리할 작업을 대기열에 저장합니다.
     */
    public void put(Executable executable) {
        try {
            queue.put(executable);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Parser가 처리할 작업을 대기열에서 가져옵니다.
     */
    public Executable take() throws InterruptedException {
        return queue.take();
    }

    public boolean isNotEmpty() {
        return !queue.isEmpty();
    }
}
