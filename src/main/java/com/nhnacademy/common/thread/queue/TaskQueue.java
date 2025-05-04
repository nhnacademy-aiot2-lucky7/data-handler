package com.nhnacademy.common.thread.queue;

import com.nhnacademy.common.thread.Executable;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 처리할 작업들을 저장하고, 순차적으로 꺼내어 <br>
 * 실행할 수 있도록 관리하는 대기열 클래스입니다.
 */
public abstract class TaskQueue {

    /**
     * Task Queue
     */
    private final BlockingQueue<Executable> queue;

    /**
     * Task Running Status
     */
    private final AtomicBoolean running;

    public TaskQueue(int capacity, AtomicBoolean running) {
        this.queue = new LinkedBlockingQueue<>(capacity);
        this.running = running;
    }

    /**
     * 처리할 작업을 대기열에 저장합니다.
     */
    public void put(Executable executable) throws InterruptedException {
        if (isNotRunning()) return;
        queue.put(executable);
    }

    /**
     * 처리할 작업을 대기열에 저장하는 것을 시도합니다. (5초)
     */
    public boolean offer(Executable executable) throws InterruptedException {
        if (isNotRunning()) return false;
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
