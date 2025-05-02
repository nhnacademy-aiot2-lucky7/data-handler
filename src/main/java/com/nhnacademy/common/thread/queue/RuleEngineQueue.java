package com.nhnacademy.common.thread.queue;

import com.nhnacademy.common.parser.dto.ParsingData;
import com.nhnacademy.common.thread.Executable;
import com.nhnacademy.common.thread.properties.RuleEngineThreadPoolProperties;
import com.nhnacademy.common.thread.runnable.RuleEngineTask;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Rule Engine에 처리할 작업들을 저장하고, 순차적으로 꺼내어 <br>
 * 실행할 수 있도록 관리하는 대기열 클래스입니다.
 * <hr>
 *
 * @see RuleEngineTask
 */
@Component
public final class RuleEngineQueue {

    /**
     * Rule Engine Queue
     */
    private final BlockingQueue<Executable> queue;

    public RuleEngineQueue(RuleEngineThreadPoolProperties properties) {
        this.queue = new LinkedBlockingQueue<>(properties.getCapacity());
    }

    /**
     * Rule Engine에 처리할 작업을 대기열에 저장합니다.
     */
    public void put(Executable executable) {
        try {
            queue.put(executable);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Rule Engine에 처리할 작업을 대기열에서 가져옵니다.
     */
    public Executable take() throws InterruptedException {
        return queue.take();
    }

    public boolean isNotEmpty() {
        return !queue.isEmpty();
    }
}
