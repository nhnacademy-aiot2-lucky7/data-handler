package com.nhnacademy.common.config;

import com.nhnacademy.common.thread.exception.CustomUncaughtExceptionHandler;
import com.nhnacademy.common.thread.properties.InfluxDBThreadPoolProperties;
import com.nhnacademy.common.thread.properties.ParserThreadPoolProperties;
import com.nhnacademy.common.thread.properties.RuleEngineThreadPoolProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public AtomicBoolean parserTaskRunning() {
        return new AtomicBoolean(true);
    }

    @Bean("influxDBTaskRunning")
    public AtomicBoolean influxDBTaskRunning() {
        return new AtomicBoolean(influxDBThreadPoolProperties.isStart());
    }

    @Bean("ruleEngineTaskRunning")
    public AtomicBoolean ruleEngineTaskRunning() {
        return new AtomicBoolean(ruleEngineThreadPoolProperties.isStart());
    }

    /**
     * Parser 작업을 처리하는 고정 크기의 스레드 풀을 생성합니다.
     */
    @Bean("parserExecutor")
    public ExecutorService parserExecutor() {
        return new ThreadPoolExecutor(
                parserThreadPoolProperties.getCorePoolSize(),
                parserThreadPoolProperties.getMaximumPoolSize(),
                parserThreadPoolProperties.getKeepAliveTime(),
                parserThreadPoolProperties.getTimeUnit(),
                new LinkedBlockingQueue<>(5),
                getCustomThreadFactory()
        );
    }

    /**
     * InfluxDB 작업을 처리하는 고정 크기의 스레드 풀을 생성합니다.
     */
    @Bean("influxDBExecutor")
    public ExecutorService influxDBExecutor() {
        return new ThreadPoolExecutor(
                influxDBThreadPoolProperties.getCorePoolSize(),
                influxDBThreadPoolProperties.getMaximumPoolSize(),
                influxDBThreadPoolProperties.getKeepAliveTime(),
                influxDBThreadPoolProperties.getTimeUnit(),
                new LinkedBlockingQueue<>(5),
                getCustomThreadFactory()
        );
    }

    /**
     * Rule Engine 작업을 처리하는 고정 크기의 스레드 풀을 생성합니다.
     */
    @Bean("ruleEngineExecutor")
    public ExecutorService ruleEngineExecutor() {
        return new ThreadPoolExecutor(
                ruleEngineThreadPoolProperties.getCorePoolSize(),
                ruleEngineThreadPoolProperties.getMaximumPoolSize(),
                ruleEngineThreadPoolProperties.getKeepAliveTime(),
                ruleEngineThreadPoolProperties.getTimeUnit(),
                new LinkedBlockingQueue<>(5),
                getCustomThreadFactory()
        );
    }

    private ThreadFactory getCustomThreadFactory() {
        return r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setUncaughtExceptionHandler(new CustomUncaughtExceptionHandler());
            return thread;
        };
    }
}
