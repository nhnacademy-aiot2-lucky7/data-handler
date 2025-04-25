package com.nhnacademy.data.common.thread.runnable;

import com.nhnacademy.data.common.thread.SensorData;
import com.nhnacademy.data.common.thread.queue.InfluxDBQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ConsumeInfluxDBQueue implements Runnable {

    private final InfluxDBQueue influxDBQueue;

    public ConsumeInfluxDBQueue(InfluxDBQueue influxDBQueue) {
        this.influxDBQueue = influxDBQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            SensorData result = influxDBQueue.take();
            /// TODO: InfluxDB Service 기능 추가
            log.info("result: {}", result);
        }
    }


}
