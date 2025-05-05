package com.nhnacademy.common.thread.queue;

import com.nhnacademy.common.thread.Executable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 처리할 작업들을 저장하고, 순차적으로 꺼내어 <br>
 * 실행할 수 있도록 관리하는 대기열 클래스입니다.
 */
@Slf4j
public abstract class TaskQueue {

    /**
     * Task Queue
     */
    private final BlockingQueue<Executable> queue;

    /**
     * Task Running Status
     */
    private final AtomicBoolean running;

    protected TaskQueue(int capacity, AtomicBoolean running) {
        this.queue = new LinkedBlockingQueue<>(capacity);
        this.running = running;
    }

    /**
     * @deprecated : 큐가 가득 차면 무기한 블로킹되는 문제로 인해, 더 이상 사용하지 않습니다.
     */
    private void put(Executable executable) throws InterruptedException {
        if (isNotRunning()) return;
        queue.put(executable);
    }

    /**
     * 처리할 작업을 대기열에 저장하는 것을 시도합니다. (5초)
     */
    public boolean offer(Executable executable) throws InterruptedException {
        if (isNotRunning()) return false;
        if (queue.remainingCapacity() < 10) {
            log.warn("남은 공간 부족: {}", queue.remainingCapacity());
        }
        return queue.offer(executable, 5, TimeUnit.SECONDS);
    }

    /**
     * 처리할 작업을 대기열에서 가져옵니다.
     */
    public Executable take() throws InterruptedException {
        return queue.take();
    }

    public boolean isNotEmpty() {
        return !queue.isEmpty();
    }

    private boolean isNotRunning() {
        return !running.get();
    }
}
