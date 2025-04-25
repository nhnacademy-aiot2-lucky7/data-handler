package com.nhnacademy.data.common.thread.starter;

import com.nhnacademy.data.common.thread.queue.ParserQueue;
import com.nhnacademy.data.common.thread.runnable.ConsumeParserQueue;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
public final class ParserExecutorStarter {

    private final ExecutorService parserExecutor;

    private final ParserQueue parserQueue;

    public ParserExecutorStarter(
            @Qualifier("parserExecutor") ExecutorService parserExecutor,
            ParserQueue parserQueue
    ) {
        this.parserExecutor = parserExecutor;
        this.parserQueue = parserQueue;
    }

    @PostConstruct
    private void start() {
        parserExecutor.submit(
                new ConsumeParserQueue(parserQueue)
        );
    }
}
