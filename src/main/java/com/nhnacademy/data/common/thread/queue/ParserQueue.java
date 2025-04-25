package com.nhnacademy.data.common.thread.queue;

import com.nhnacademy.data.common.thread.Executable;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public final class ParserQueue {

    private final BlockingQueue<Executable> parserJobQueue = new LinkedBlockingQueue<>(50);

    public void put(Executable job) {
        try {
            parserJobQueue.put(job);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Executable take() {
        Executable job = null;
        try {
            job = parserJobQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return job;
    }
}
