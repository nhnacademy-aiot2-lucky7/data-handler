package com.nhnacademy.common.thread.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CustomUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof InterruptedException) {
            log.error("{}", e.getMessage(), e);
            t.interrupt();
        } else {
            log.warn("{}", e.getMessage(), e);
        }
    }
}
