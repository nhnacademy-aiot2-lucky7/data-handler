package com.nhnacademy.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
@Deprecated
public class AsyncConfig {

    // MQTT 수신 처리용 ThreadPool
    /*@Bean("mqttExecutor")
    public Executor mqttExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("MQTT-");
        executor.initialize();
        return executor;
    }

    // Influx 저장 처리용 ThreadPool
    @Bean("influxExecutor")
    public Executor influxExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("Influx-");
        executor.initialize();
        return executor;
    }*/
}
