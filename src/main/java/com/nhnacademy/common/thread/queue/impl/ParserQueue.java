package com.nhnacademy.common.thread.queue.impl;

import com.nhnacademy.common.thread.properties.ParserThreadPoolProperties;
import com.nhnacademy.common.thread.queue.TaskQueue;
import com.nhnacademy.common.thread.runnable.impl.ParserTask;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Parser가 처리할 작업들을 저장하고, 순차적으로 꺼내어 <br>
 * 실행할 수 있도록 관리하는 대기열 클래스입니다.
 * <hr>
 *
 * @see ParserTask
 */
@Component
public final class ParserQueue extends TaskQueue {

    public ParserQueue(
            ParserThreadPoolProperties properties,
            @Qualifier("parserTaskRunning") AtomicBoolean running
    ) {
        super(properties.getCapacity(), running);
    }
}
