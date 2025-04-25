package com.nhnacademy.data.common.thread.pool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolConfig {

    @Bean("parserExecutor")
    public ExecutorService parserExecutor() {
        return Executors.newFixedThreadPool(10);
    }

    @Bean("influxDBExecutor")
    public ExecutorService influxDBExecutor() {
        return Executors.newFixedThreadPool(5);
    }

    @Bean("ruleEngineExecutor")
    public ExecutorService ruleEngineExecutor() {
        return Executors.newFixedThreadPool(5);
    }
}
