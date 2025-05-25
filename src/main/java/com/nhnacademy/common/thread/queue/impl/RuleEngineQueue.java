package com.nhnacademy.common.thread.queue.impl;

import com.nhnacademy.common.thread.properties.RuleEngineThreadPoolProperties;
import com.nhnacademy.common.thread.queue.TaskQueue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Rule Engine에 처리할 작업들을 저장하고, 순차적으로 꺼내어 <br>
 * 실행할 수 있도록 관리하는 대기열 클래스입니다.
 */
@Component
public final class RuleEngineQueue extends TaskQueue {

    public RuleEngineQueue(
            RuleEngineThreadPoolProperties properties,
            @Qualifier("ruleEngineTaskRunning") AtomicBoolean running
    ) {
        super(properties.getCapacity(), running);
    }
}
