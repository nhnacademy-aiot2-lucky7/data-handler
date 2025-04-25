package com.nhnacademy.data.common.thread.queue;

import com.nhnacademy.data.common.thread.SensorData;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public final class RuleEngineQueue {

    private final BlockingQueue<SensorData> ruleEngineQueue = new LinkedBlockingQueue<>(25);

    public void put(SensorData result) {
        try {
            ruleEngineQueue.put(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public SensorData take() {
        SensorData result = null;
        try {
            result = ruleEngineQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return result;
    }
}
