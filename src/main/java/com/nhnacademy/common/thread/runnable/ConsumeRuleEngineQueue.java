package com.nhnacademy.common.thread.runnable;

import com.nhnacademy.common.thread.queue.RuleEngineQueue;
import com.nhnacademy.database.SensorData;
import com.nhnacademy.rule.DataDTO;
import com.nhnacademy.rule.RuleEngine;

/**
 * Rule Engine 전용 작업 Queue({@link RuleEngineQueue})에서 작업을 꺼내 <br>
 * 실행하는 {@link Runnable} 구현 클래스입니다.
 * <hr>
 * 이 클래스는 Rule Engine 전용 Thread Pool에서 주기적으로 실행되며, <br>
 * Queue에 쌓인 작업을 하나씩 가져와 처리합니다.
 *
 * @see com.nhnacademy.common.thread.pool.ThreadPoolConfig
 */
public final class ConsumeRuleEngineQueue implements Runnable {

    /**
     * Rule Engine 작업 대기열
     */
    private final RuleEngineQueue ruleEngineQueue;

    private final RuleEngine ruleEngine;

    public ConsumeRuleEngineQueue(
            RuleEngineQueue ruleEngineQueue,
            RuleEngine ruleEngine
    ) {
        this.ruleEngineQueue = ruleEngineQueue;
        this.ruleEngine = ruleEngine;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            SensorData data = ruleEngineQueue.take();
            ruleEngine.sendData(
                    new DataDTO(
                            data.getDeviceId(),
                            data.getDataType(),
                            data.getLocation(),
                            data.getData().get("value"),
                            (long) data.getData().get("time")
                    )
            );
        }
    }
}
