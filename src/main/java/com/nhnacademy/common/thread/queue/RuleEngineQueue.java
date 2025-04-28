package com.nhnacademy.common.thread.queue;

import com.nhnacademy.database.SensorData;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Rule Engine에 처리할 작업들을 저장하고, 순차적으로 꺼내어 <br>
 * 실행할 수 있도록 관리하는 대기열 클래스입니다.
 * <hr>
 *
 * @see com.nhnacademy.common.thread.runnable.ConsumeRuleEngineQueue
 */
@Component
public final class RuleEngineQueue {

    /**
     * Rule Engine Queue
     */
    private final BlockingQueue<SensorData> queue = new LinkedBlockingQueue<>(100);

    /**
     * Rule Engine에 처리할 작업을 대기열에 저장합니다.
     */
    public void put(SensorData data) {
        try {
            queue.put(data);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Rule Engine에 처리할 작업을 대기열에서 가져옵니다.
     */
    public SensorData take() {
        SensorData data = null;
        try {
            data = queue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return data;
    }
}
