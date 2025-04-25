package com.nhnacademy.data.common.thread.starter;

import com.nhnacademy.data.common.thread.queue.InfluxDBQueue;
import com.nhnacademy.data.common.thread.runnable.ConsumeInfluxDBQueue;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
public final class InfluxDBExecutorStarter {

    private final ExecutorService influxDBExecutor;

    private final InfluxDBQueue influxDBQueue;

    public InfluxDBExecutorStarter(
            @Qualifier("influxDBExecutor") ExecutorService influxDBExecutor,
            InfluxDBQueue influxDBQueue
    ) {
        this.influxDBExecutor = influxDBExecutor;
        this.influxDBQueue = influxDBQueue;
    }

    @PostConstruct
    private void start() {
        influxDBExecutor.submit(
                new ConsumeInfluxDBQueue(influxDBQueue)
        );
    }
}
