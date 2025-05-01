package com.nhnacademy.common.thread.properties;

import com.nhnacademy.common.thread.pool.ThreadPoolConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Rule Engine 전용 Thread Pool의 실행 파라미터를 외부 설정 파일(application.yml 등)로부터 주입받아 관리하는 클래스입니다.
 * <p>
 * 주로 {@link java.util.concurrent.ThreadPoolExecutor} 생성 시 필요한 <br>
 * corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, queueCapacity 값을 설정하기 위해 사용됩니다.
 * </p>
 * <hr>
 * <h2>설정 항목</h2>
 * <ul>
 *     <li>corePoolSize : Thread Pool에서 항상 유지할 최소 스레드 수</li>
 *     <li>maximumPoolSize : Thread Pool에서 허용하는 최대 스레드 수</li>
 *     <li>keepAliveTime : corePoolSize 초과 스레드의 유휴 시간</li>
 *     <li>timeUnit : keepAliveTime의 시간 단위</li>
 *     <li>queueCapacity : 작업 대기열의 최대 수용량</li>
 * </ul>
 * <hr>
 * <h2>주의사항</h2>
 * <ul>
 *     <li>{@code maximumPoolSize}는 {@code corePoolSize}보다 작을 수 없습니다.</li>
 *     <li>{@code queueCapacity}를 0 이하로 설정할 경우, 큐가 아닌 직접 실행 전략(Direct Handoff)으로 동작합니다.</li>
 * </ul>
 *
 * @see java.util.concurrent.ThreadPoolExecutor
 * @see ThreadPoolConfig#ruleEngineExecutor()
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "thread-pool.rule-engine")
public class RuleEngineThreadPoolProperties {

    /**
     * Parser의 Thread Pool에서 항상 유지할 최소 스레드 수입니다. <br>
     * core-pool-size 이하로는 스레드가 줄어들지 않으며, 기본적으로 1로 설정됩니다.
     * <hr>
     * <b>Default</b>: 1
     */
    private int corePoolSize = 1;

    /**
     * Parser의 Thread Pool에서 허용하는 최대 스레드 수입니다. <br>
     * 동시 작업 수가 많아지면 core-pool-size를 초과하여 이 수만큼 스레드를 생성할 수 있습니다.
     * <hr>
     * <b>Default</b>: 2
     */
    private int maximumPoolSize = 2;

    /**
     * core-pool-size를 초과하는 스레드가 유휴 상태일 때, 종료되기까지 기다리는 시간입니다.
     * 주로 일시적인 트래픽 증가에 대응 후, 스레드 리소스를 정리하는 데 사용됩니다.
     * <hr>
     * <b>Default</b>: 5
     *
     * @see RuleEngineThreadPoolProperties#corePoolSize
     */
    private long keepAliveTime = 5;

    /**
     * keep-alive-time 값의 시간 단위를 지정합니다. <br>
     * 예를 들어 분(MINUTES), 초(SECONDS), 밀리초(MILLISECONDS) 등으로 설정할 수 있습니다.
     * <hr>
     * <b>Default</b>: TimeUnit.MINUTES (분 단위)
     */
    private TimeUnit timeUnit = TimeUnit.MINUTES;

    /**
     * 작업 대기열(BlockingQueue)의 최대 수용 용량입니다. <br>
     * 큐가 가득 차면 새 작업을 작업 대기열에 넣을 수 없습니다.
     * <hr>
     * <b>주의</b>: 0 이하로 설정하면 큐에 작업이 쌓이지 않고 바로 실행만 시도합니다.
     */
    private int capacity = 100;
}
