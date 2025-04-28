package com.nhnacademy.common.thread.queue;

import com.nhnacademy.database.SensorData;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * InfluxDB에 처리할 작업들을 저장하고, 순차적으로 꺼내어 <br>
 * 실행할 수 있도록 관리하는 대기열 클래스입니다.
 * <hr>
 *
 * @see com.nhnacademy.common.thread.runnable.ConsumeInfluxDBQueue
 */
@Component
public final class InfluxDBQueue {

    /**
     * InfluxDB Queue
     */
    private final BlockingQueue<SensorData> queue = new LinkedBlockingQueue<>(100);

    /**
     * InfluxDB에 처리할 작업을 대기열에 저장합니다.
     */
    public void put(SensorData data) {
        try {
            queue.put(data);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * InfluxDB에 처리할 작업을 대기열에서 가져옵니다.
     */
    public SensorData take() throws InterruptedException {
        return queue.take();
    }
}
