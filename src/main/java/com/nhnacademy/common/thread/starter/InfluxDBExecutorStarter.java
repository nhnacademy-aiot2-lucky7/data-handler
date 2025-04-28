package com.nhnacademy.common.thread.starter;

import com.nhnacademy.common.thread.queue.InfluxDBQueue;
import com.nhnacademy.common.thread.runnable.ConsumeInfluxDBQueue;
import com.nhnacademy.database.influxdb.repository.InfluxDBRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
public final class InfluxDBExecutorStarter {

    private final ExecutorService influxDBExecutor;

    private final InfluxDBQueue influxDBQueue;

    private final InfluxDBRepository influxDBRepository;

    public InfluxDBExecutorStarter(
            @Qualifier("influxDBExecutor") ExecutorService influxDBExecutor,
            InfluxDBQueue influxDBQueue, InfluxDBRepository influxDBRepository
    ) {
        this.influxDBExecutor = influxDBExecutor;
        this.influxDBQueue = influxDBQueue;
        this.influxDBRepository = influxDBRepository;
    }

    @PostConstruct
    private void start() {
        influxDBExecutor.submit(
                new ConsumeInfluxDBQueue(influxDBQueue, influxDBRepository)
        );
    }
}
