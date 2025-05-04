package com.nhnacademy.common.thread.runnable.impl;

import com.nhnacademy.common.config.ThreadPoolConfig;
import com.nhnacademy.common.thread.Executable;
import com.nhnacademy.common.thread.queue.impl.InfluxDBQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * InfluxDB 전용 작업 Queue({@link InfluxDBQueue})에서 작업을 꺼내 <br>
 * 실행하는 {@link Runnable} 구현 클래스입니다.
 * <hr>
 * 이 클래스는 InfluxDB 전용 Thread Pool에서 주기적으로 실행되며, <br>
 * Queue에 쌓인 작업을 하나씩 가져와 처리합니다.
 *
 * @see ThreadPoolConfig
 */
@Deprecated
@Slf4j
public final class InfluxDBTask implements Runnable {

    /**
     * InfluxDB 작업 대기열
     */
    private final InfluxDBQueue influxDBQueue;

    private final AtomicBoolean running;

    public InfluxDBTask(InfluxDBQueue influxDBQueue, AtomicBoolean running) {
        this.influxDBQueue = influxDBQueue;
        this.running = running;
    }

    @Override
    public void run() {
        Thread influxDBThread = Thread.currentThread();
        while (!influxDBThread.isInterrupted()
                && (running.get() || influxDBQueue.isNotEmpty())
        ) {
            try {
                Executable influxDB = influxDBQueue.take();
                influxDB.execute();
            } catch (InterruptedException e) {
                log.error("Thread interrupted: {}", e.getMessage());
                influxDBThread.interrupt();
            } catch (Throwable e) {
                log.warn("{}", e.getMessage(), e);
            }
        }
    }
}
