package com.nhnacademy.common.thread.shutdown.impl;

import com.nhnacademy.common.thread.pool.ThreadPoolConfig;
import com.nhnacademy.common.thread.shutdown.AbstractExecutorShutdown;
import com.nhnacademy.influxdb.InfluxDBManagement;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public final class InfluxDBExecutorShutdown extends AbstractExecutorShutdown {

    private final ExecutorService influxDBExecutor;

    private final InfluxDBManagement influxDBManagement;

    /**
     * @param influxDBExecutor {@link ThreadPoolConfig#influxDBExecutor()}
     */
    public InfluxDBExecutorShutdown(
            @Qualifier("influxDBExecutor") ExecutorService influxDBExecutor,
            InfluxDBManagement influxDBManagement
    ) {
        this.influxDBExecutor = influxDBExecutor;
        this.influxDBManagement = influxDBManagement;
    }

    @PreDestroy
    public void shutdown() {
        shutdownExecutor(influxDBExecutor, "influxDBExecutor");
        influxDBManagement.allClose();
    }
}
