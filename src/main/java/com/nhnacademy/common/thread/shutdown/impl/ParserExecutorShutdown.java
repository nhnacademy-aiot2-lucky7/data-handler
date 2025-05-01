package com.nhnacademy.common.thread.shutdown.impl;

import com.nhnacademy.broker.mqtt.MqttManagement;
import com.nhnacademy.common.thread.pool.ThreadPoolConfig;
import com.nhnacademy.common.thread.shutdown.AbstractExecutorShutdown;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public final class ParserExecutorShutdown extends AbstractExecutorShutdown {

    private final ExecutorService parserExecutor;

    private final MqttManagement mqttManagement;

    /**
     * @param parserExecutor {@link ThreadPoolConfig#parserExecutor()}
     */
    public ParserExecutorShutdown(
            @Qualifier("parserExecutor") ExecutorService parserExecutor,
            MqttManagement mqttManagement
    ) {
        this.parserExecutor = parserExecutor;
        this.mqttManagement = mqttManagement;
    }

    @PreDestroy
    public void shutdown() {
        mqttManagement.close();
        shutdownExecutor(parserExecutor, "parserExecutor");
    }
}
