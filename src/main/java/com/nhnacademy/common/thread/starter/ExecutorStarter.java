package com.nhnacademy.common.thread.starter;

import jakarta.annotation.PostConstruct;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ExecutorStarter {

    private final ExecutorService executorService;

    private final AtomicBoolean running;

    protected ExecutorStarter(ExecutorService executorService, AtomicBoolean running) {
        this.executorService = executorService;
        this.running = running;
    }

    @PostConstruct
    public void start() {
        if (!running.get()) return;
        executorService.submit(createTask());
    }

    protected abstract Runnable createTask();
}
