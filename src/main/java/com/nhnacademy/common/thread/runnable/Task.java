package com.nhnacademy.common.thread.runnable;

import com.nhnacademy.common.thread.Executable;
import com.nhnacademy.common.thread.queue.TaskQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public final class Task implements Runnable {

    /**
     * 작업 대기열
     */
    private final TaskQueue taskQueue;

    private final AtomicBoolean running;

    public Task(TaskQueue taskQueue, AtomicBoolean running) {
        this.taskQueue = taskQueue;
        this.running = running;
    }

    @Override
    public void run() {
        Thread thread = Thread.currentThread();
        while (!thread.isInterrupted()
                && (running.get()) || taskQueue.isNotEmpty()
        ) {
            try {
                Executable task = taskQueue.take();
                task.execute();
            } catch (InterruptedException e) {
                log.error("Thread interrupted: {}", e.getMessage());
                thread.interrupt();
            } catch (Throwable e) {
                log.warn("{}", e.getMessage(), e);
            }
        }
    }
}
