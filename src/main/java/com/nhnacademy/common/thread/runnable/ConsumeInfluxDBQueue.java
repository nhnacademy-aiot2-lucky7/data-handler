package com.nhnacademy.common.thread.runnable;

import com.nhnacademy.common.thread.queue.InfluxDBQueue;
import com.nhnacademy.database.SensorData;
import com.nhnacademy.database.influxdb.repository.InfluxDBRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * InfluxDB 전용 작업 Queue({@link InfluxDBQueue})에서 작업을 꺼내 <br>
 * 실행하는 {@link Runnable} 구현 클래스입니다.
 * <hr>
 * 이 클래스는 InfluxDB 전용 Thread Pool에서 주기적으로 실행되며, <br>
 * Queue에 쌓인 작업을 하나씩 가져와 처리합니다.
 *
 * @see com.nhnacademy.common.thread.pool.ThreadPoolConfig
 */
@Slf4j
public final class ConsumeInfluxDBQueue implements Runnable {

    private static final int MAX_RETRIES = 3;

    private static final long RETRY_DELAY_MS = 100L;

    /**
     * InfluxDB 작업 대기열
     */
    private final InfluxDBQueue influxDBQueue;

    /**
     * InfluxDB
     */
    private final InfluxDBRepository influxDBRepository;

    public ConsumeInfluxDBQueue(
            InfluxDBQueue influxDBQueue,
            InfluxDBRepository influxDBRepository
    ) {
        this.influxDBQueue = influxDBQueue;
        this.influxDBRepository = influxDBRepository;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                SensorData data = influxDBQueue.take();
                try {
                    execute(data);
                } catch (Exception e) {
                    log.error("Failed to save SensorData after {} retries: {}", MAX_RETRIES, e.getMessage(), e);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("ConsumeInfluxDBQueue thread interrupted, shutting down.");
        }
    }

    private void execute(SensorData data) throws Exception {
        int retryCount = 0;
        while (true) {
            try {
                influxDBRepository.save(data);
                return;
            } catch (Exception e) {
                retryCount++;
                if (retryCount > MAX_RETRIES) {
                    throw e;
                }
                log.warn("Failed to save SensorData, retrying {}/{}...", retryCount, MAX_RETRIES);
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    throw interrupted; // 현재 쓰레드를 제대로 끊어줍니다.
                }
            }
        }
    }
}
