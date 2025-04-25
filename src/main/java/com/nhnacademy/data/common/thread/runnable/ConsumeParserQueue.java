package com.nhnacademy.data.common.thread.runnable;

import com.nhnacademy.data.common.thread.Executable;
import com.nhnacademy.data.common.thread.queue.ParserQueue;

public final class ConsumeParserQueue implements Runnable {

    private final ParserQueue parserQueue;

    public ConsumeParserQueue(ParserQueue parserQueue) {
        this.parserQueue = parserQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Executable job = parserQueue.take();
            job.execute();
        }
    }
}
