package com.nhnacademy.data.common.thread.runnable;

import com.nhnacademy.data.common.thread.SensorData;
import com.nhnacademy.data.common.thread.queue.RuleEngineQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ConsumeRuleEngineQueue implements Runnable {

    private final RuleEngineQueue ruleEngineQueue;

    public ConsumeRuleEngineQueue(RuleEngineQueue ruleEngineQueue) {
        this.ruleEngineQueue = ruleEngineQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            SensorData result = ruleEngineQueue.take();
            /// TODO: InfluxDB Service 기능 추가
            log.info("result: {}", result);
        }
    }
}
