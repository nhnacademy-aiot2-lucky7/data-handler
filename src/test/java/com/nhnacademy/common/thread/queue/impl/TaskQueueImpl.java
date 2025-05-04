package com.nhnacademy.common.thread.queue.impl;

import com.nhnacademy.common.thread.queue.TaskQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@code TaskQueue}를 테스트하기 위한 클래스입니다.
 *
 * @see TaskQueue
 */
@Slf4j
public final class TaskQueueImpl extends TaskQueue {

    public TaskQueueImpl(int capacity, AtomicBoolean running) {
        super(capacity, running);
    }

    /**
     * 초기에 지정된 capacity 만큼, 작업을 추가합니다.
     */
    public void addTask() {
        try {
            for (int n = 0; n < getCapacity(); n++) {
                final int index = n;
                offer(() -> log.info("작업 실행: {}", index));
            }
        } catch (InterruptedException e) {
            log.error("{}", e.getMessage(), e);
        }
    }

    /**
     * 부모 필드에 존재하는 queue를 가져옵니다.
     */
    public BlockingQueue<?> getFieldQueue() {
        return (BlockingQueue<?>)
                Optional.ofNullable(ReflectionTestUtils.getField(this, "queue"))
                        .orElseThrow(() -> new NullPointerException("생성자에서 queue가 할당되지 않았습니다."));
    }

    /**
     * 부모 필드에 존재하는 running을 가져옵니다.
     */
    public AtomicBoolean getFieldRunning() {
        return (AtomicBoolean)
                Optional.ofNullable(ReflectionTestUtils.getField(this, "running"))
                        .orElseThrow(() -> new NullPointerException("생성자에서 running이 할당되지 않았습니다."));
    }

    /**
     * queue의 size를 반환합니다.
     */
    public int getSize() {
        return getFieldQueue().size();
    }

    /**
     * queue의 capacity를 반환합니다.
     */
    public int getCapacity() {
        BlockingQueue<?> queue = getFieldQueue();
        return queue.remainingCapacity() + queue.size();
    }
}
