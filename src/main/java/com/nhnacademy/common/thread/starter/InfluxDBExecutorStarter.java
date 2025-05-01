package com.nhnacademy.common.thread.starter;

import com.nhnacademy.common.thread.queue.InfluxDBQueue;
import com.nhnacademy.common.thread.runnable.InfluxDBTask;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public final class InfluxDBExecutorStarter {

    private final ExecutorService influxDBExecutor;

    private final InfluxDBQueue influxDBQueue;

    private final AtomicBoolean running;

    public InfluxDBExecutorStarter(
            @Qualifier("influxDBExecutor") ExecutorService influxDBExecutor,
            @Qualifier("influxDBTaskRunning") AtomicBoolean running,
            InfluxDBQueue influxDBQueue
    ) {
        this.influxDBExecutor = influxDBExecutor;
        this.influxDBQueue = influxDBQueue;
        this.running = running;
    }

    @PostConstruct
    private void start() {
        influxDBExecutor.submit(
                new InfluxDBTask(influxDBQueue, running)
        );
    }
}
