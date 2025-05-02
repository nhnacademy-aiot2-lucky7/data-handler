package com.nhnacademy.common.thread.queue;

import com.nhnacademy.common.thread.Executable;
import com.nhnacademy.common.thread.properties.InfluxDBThreadPoolProperties;
import com.nhnacademy.common.thread.runnable.InfluxDBTask;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * InfluxDB에 처리할 작업들을 저장하고, 순차적으로 꺼내어 <br>
 * 실행할 수 있도록 관리하는 대기열 클래스입니다.
 * <hr>
 *
 * @see InfluxDBTask
 */
@Component
public final class InfluxDBQueue {

    /**
     * InfluxDB Queue
     */
    private final BlockingQueue<Executable> queue;

    public InfluxDBQueue(InfluxDBThreadPoolProperties properties) {
        this.queue = new LinkedBlockingQueue<>(properties.getCapacity());
    }

    /**
     * InfluxDB에 처리할 작업을 대기열에 저장합니다.
     */
    public void put(Executable executable) {
        try {
            queue.put(executable);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * InfluxDB에 처리할 작업을 대기열에서 가져옵니다.
     */
    public Executable take() throws InterruptedException {
        return queue.take();
    }

    public boolean isNotEmpty() {
        return !queue.isEmpty();
    }
}
