package com.nhnacademy.common.config;

import com.nhnacademy.common.thread.exception.CustomUncaughtExceptionHandler;
import com.nhnacademy.common.thread.properties.InfluxDBThreadPoolProperties;
import com.nhnacademy.common.thread.properties.ParserThreadPoolProperties;
import com.nhnacademy.common.thread.properties.RuleEngineThreadPoolProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 각 영역의 Thread Pool을 생성하는 설정 클래스입니다.
 *
 * @see ParserThreadPoolProperties
 * @see InfluxDBThreadPoolProperties
 * @see RuleEngineThreadPoolProperties
 */
@Configuration
public class ThreadPoolConfig {

    private final ParserThreadPoolProperties parserThreadPoolProperties;

    private final InfluxDBThreadPoolProperties influxDBThreadPoolProperties;

    private final RuleEngineThreadPoolProperties ruleEngineThreadPoolProperties;

    public ThreadPoolConfig(
            ParserThreadPoolProperties parserThreadPoolProperties,
            InfluxDBThreadPoolProperties influxDBThreadPoolProperties,
            RuleEngineThreadPoolProperties ruleEngineThreadPoolProperties
    ) {
        this.parserThreadPoolProperties = parserThreadPoolProperties;
        this.influxDBThreadPoolProperties = influxDBThreadPoolProperties;
        this.ruleEngineThreadPoolProperties = ruleEngineThreadPoolProperties;
    }

    @Bean("parserTaskRunning")
    AtomicBoolean parserTaskRunning() {
        return new AtomicBoolean(true);
    }

    @Bean("influxDBTaskRunning")
    AtomicBoolean influxDBTaskRunning() {
        return new AtomicBoolean(influxDBThreadPoolProperties.isStart());
    }

    @Bean("ruleEngineTaskRunning")
    AtomicBoolean ruleEngineTaskRunning() {
        return new AtomicBoolean(ruleEngineThreadPoolProperties.isStart());
    }

    /**
     * Parser 작업을 처리하는 고정 크기의 스레드 풀을 생성합니다.
     */
    @Bean("parserExecutor")
    ExecutorService parserExecutor() {
        return new ThreadPoolExecutor(
                parserThreadPoolProperties.getCorePoolSize(),
                parserThreadPoolProperties.getMaximumPoolSize(),
                parserThreadPoolProperties.getKeepAliveTime(),
                parserThreadPoolProperties.getTimeUnit(),
                new LinkedBlockingQueue<>(5),
                getCustomThreadFactory(new AtomicInteger(), "parser")
        );
    }

    /**
     * InfluxDB 작업을 처리하는 고정 크기의 스레드 풀을 생성합니다.
     */
    @Bean("influxDBExecutor")
    ExecutorService influxDBExecutor() {
        return new ThreadPoolExecutor(
                influxDBThreadPoolProperties.getCorePoolSize(),
                influxDBThreadPoolProperties.getMaximumPoolSize(),
                influxDBThreadPoolProperties.getKeepAliveTime(),
                influxDBThreadPoolProperties.getTimeUnit(),
                new LinkedBlockingQueue<>(5),
                getCustomThreadFactory(new AtomicInteger(), "influx")
        );
    }

    /**
     * Rule Engine 작업을 처리하는 고정 크기의 스레드 풀을 생성합니다.
     */
    @Bean("ruleEngineExecutor")
    ExecutorService ruleEngineExecutor() {
        return new ThreadPoolExecutor(
                ruleEngineThreadPoolProperties.getCorePoolSize(),
                ruleEngineThreadPoolProperties.getMaximumPoolSize(),
                ruleEngineThreadPoolProperties.getKeepAliveTime(),
                ruleEngineThreadPoolProperties.getTimeUnit(),
                new LinkedBlockingQueue<>(5),
                getCustomThreadFactory(new AtomicInteger(), "rule")
        );
    }

    private ThreadFactory getCustomThreadFactory(AtomicInteger threadNumber, String prefix) {
        return r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setName("%s-pool-%d".formatted(prefix, threadNumber.incrementAndGet()));
            thread.setUncaughtExceptionHandler(new CustomUncaughtExceptionHandler());
            return thread;
        };
    }
}
