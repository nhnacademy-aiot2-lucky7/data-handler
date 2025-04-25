package com.nhnacademy.data.common.thread.queue;

import com.nhnacademy.data.common.thread.SensorData;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public final class InfluxDBQueue {

    private final BlockingQueue<SensorData> influxQueue = new LinkedBlockingQueue<>(25);

    public void put(SensorData result) {
        try {
            influxQueue.put(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public SensorData take() {
        SensorData result = null;
        try {
            result = influxQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return result;
    }
}
