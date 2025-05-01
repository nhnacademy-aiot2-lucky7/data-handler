package com.nhnacademy.common.thread;

public interface Executable {

    void execute();

    default int getRetryCount() {
        return Integer.MAX_VALUE;
    }

    default void incrementRetryCount() {
    }
}
