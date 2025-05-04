package com.nhnacademy.common.thread.starter.impl;

import com.nhnacademy.common.thread.queue.impl.InfluxDBQueue;
import com.nhnacademy.common.thread.runnable.Task;
import com.nhnacademy.common.thread.starter.ExecutorStarter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public final class InfluxDBExecutorStarter extends ExecutorStarter {

    private final InfluxDBQueue influxDBQueue;

    private final AtomicBoolean running;

    public InfluxDBExecutorStarter(
            @Qualifier("influxDBExecutor") ExecutorService influxDBExecutor,
            @Qualifier("influxDBTaskRunning") AtomicBoolean running,
            InfluxDBQueue influxDBQueue
    ) {
        super(influxDBExecutor, running);
        this.influxDBQueue = influxDBQueue;
        this.running = running;
    }

    @Override
    protected Runnable createTask() {
        return new Task(influxDBQueue, running);
    }
}
