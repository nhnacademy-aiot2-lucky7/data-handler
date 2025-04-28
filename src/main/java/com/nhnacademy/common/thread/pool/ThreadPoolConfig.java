package com.nhnacademy.common.thread.pool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 각 영역의 Thread Pool을 생성하는 설정 클래스입니다.
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 파서(Parser) 작업을 처리하는 고정 크기의 스레드 풀을 생성합니다.
     */
    @Bean("parserExecutor")
    public ExecutorService parserExecutor() {
        return Executors.newFixedThreadPool(4);
    }

    /**
     * InfluxDB 작업을 처리하는 고정 크기의 스레드 풀을 생성합니다.
     */
    @Bean("influxDBExecutor")
    public ExecutorService influxDBExecutor() {
        return Executors.newFixedThreadPool(2);
    }

    /**
     * Rule Engine 작업을 처리하는 고정 크기의 스레드 풀을 생성합니다.
     */
    @Bean("ruleEngineExecutor")
    public ExecutorService ruleEngineExecutor() {
        return Executors.newFixedThreadPool(2);
    }
}
