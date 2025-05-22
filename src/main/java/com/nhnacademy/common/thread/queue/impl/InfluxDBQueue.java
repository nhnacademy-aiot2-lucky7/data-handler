package com.nhnacademy.common.thread.queue.impl;

import com.nhnacademy.common.thread.properties.InfluxDBThreadPoolProperties;
import com.nhnacademy.common.thread.queue.TaskQueue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * InfluxDB에 처리할 작업들을 저장하고, 순차적으로 꺼내어 <br>
 * 실행할 수 있도록 관리하는 대기열 클래스입니다.
 */
@Component
public final class InfluxDBQueue extends TaskQueue {

    public InfluxDBQueue(
            InfluxDBThreadPoolProperties properties,
            @Qualifier("influxDBTaskRunning") AtomicBoolean running
    ) {
        super(properties.getCapacity(), running);
    }
}
