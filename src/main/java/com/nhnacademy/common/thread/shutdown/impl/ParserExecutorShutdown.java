package com.nhnacademy.common.thread.shutdown.impl;

import com.nhnacademy.broker.mqtt.MqttManagement;
import com.nhnacademy.common.config.ThreadPoolConfig;
import com.nhnacademy.common.thread.shutdown.ExecutorShutdown;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public final class ParserExecutorShutdown extends ExecutorShutdown {

    private final ExecutorService parserExecutor;

    private final MqttManagement mqttManagement;

    private final AtomicBoolean running;

    private final RuleEngineExecutorShutdown ruleEngineExecutorShutdown;

    private final InfluxDBExecutorShutdown influxDBExecutorShutdown;

    /**
     * @param parserExecutor {@link ThreadPoolConfig#parserExecutor()}
     */
    public ParserExecutorShutdown(
            @Qualifier("parserExecutor") ExecutorService parserExecutor,
            MqttManagement mqttManagement,
            @Qualifier("parserTaskRunning") AtomicBoolean running,
            RuleEngineExecutorShutdown ruleEngineExecutorShutdown,
            InfluxDBExecutorShutdown influxDBExecutorShutdown
    ) {
        this.parserExecutor = parserExecutor;
        this.mqttManagement = mqttManagement;
        this.running = running;
        this.ruleEngineExecutorShutdown = ruleEngineExecutorShutdown;
        this.influxDBExecutorShutdown = influxDBExecutorShutdown;
    }

    @PreDestroy
    public void shutdown() {
        running.set(false);
        mqttManagement.close();
        shutdownExecutor(parserExecutor, "parserExecutor");
        ruleEngineExecutorShutdown.shutdown();
        influxDBExecutorShutdown.shutdown();
    }
}
