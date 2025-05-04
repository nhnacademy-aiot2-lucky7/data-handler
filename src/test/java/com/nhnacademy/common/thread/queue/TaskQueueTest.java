package com.nhnacademy.common.thread.queue;

import com.nhnacademy.common.thread.Executable;
import com.nhnacademy.common.thread.queue.impl.TaskQueueImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
class TaskQueueTest {

    private static final int TEST_CAPACITY = 5;

    private static final AtomicBoolean TEST_RUNNING = new AtomicBoolean();

    @DisplayName("TaskQueue의 기본 생성자 테스트")
    @Test
    void testConstructor() {
        TEST_RUNNING.set(true);
        TaskQueueImpl taskQueueImpl = new TaskQueueImpl(TEST_CAPACITY, TEST_RUNNING);

        int capacity = taskQueueImpl.getCapacity();
        AtomicBoolean running = taskQueueImpl.getFieldRunning();

        log.debug("capacity: {}", capacity);
        log.debug("running: {}", running);
        Assertions.assertAll(
                () -> Assertions.assertEquals(TEST_CAPACITY, capacity),
                () -> Assertions.assertEquals(TEST_RUNNING, running)
        );
    }

    @DisplayName("running이 true일 때, 작업을 큐에 삽입 테스트")
    @Test
    void testAddTask_Success() {
        TEST_RUNNING.set(true);
        TaskQueueImpl taskQueueImpl = new TaskQueueImpl(TEST_CAPACITY, TEST_RUNNING);
        taskQueueImpl.addTask();

        int size = taskQueueImpl.getSize();

        log.debug("input task success size: {}", size);
        Assertions.assertAll(
                () -> Assertions.assertTrue(taskQueueImpl.isNotEmpty()),
                () -> Assertions.assertEquals(TEST_CAPACITY, size)
        );
    }

    @DisplayName("running이 false일 때, 작업을 큐에 삽입 테스트")
    @Test
    void testAddTask_Failed() {
        TEST_RUNNING.set(false);
        TaskQueueImpl taskQueueImpl = new TaskQueueImpl(TEST_CAPACITY, TEST_RUNNING);
        taskQueueImpl.addTask();

        int size = taskQueueImpl.getSize();

        log.debug("input task failed size: {}", size);
        Assertions.assertAll(
                () -> Assertions.assertFalse(taskQueueImpl.isNotEmpty()),
                () -> Assertions.assertEquals(0, size)
        );
    }

    @DisplayName("queue의 최대 size가 가득 찼을 때, 삽입 테스트")
    @Test
    void testAddTask_SizeOut() throws InterruptedException {
        TEST_RUNNING.set(true);
        TaskQueueImpl taskQueueImpl = new TaskQueueImpl(TEST_CAPACITY, TEST_RUNNING);
        taskQueueImpl.addTask();

        Assertions.assertFalse(taskQueueImpl.offer(() -> {
        }));
    }

    @DisplayName("queue에 들어있는 작업을 꺼내는 테스트")
    @Test
    void testGetTask() throws InterruptedException {
        TEST_RUNNING.set(true);
        TaskQueueImpl taskQueueImpl = new TaskQueueImpl(TEST_CAPACITY, TEST_RUNNING);
        taskQueueImpl.addTask();

        List<Executable> tasks = new ArrayList<>();
        while (taskQueueImpl.isNotEmpty()) {
            tasks.add(taskQueueImpl.take());
        }

        tasks.forEach(Executable::execute);
        Assertions.assertEquals(TEST_CAPACITY, tasks.size());
    }
}
