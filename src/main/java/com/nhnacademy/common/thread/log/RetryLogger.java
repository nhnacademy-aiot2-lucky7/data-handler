package com.nhnacademy.common.thread.log;

import lombok.extern.slf4j.Slf4j;

/**
 * Thread 작업 수행 중 재시도(Retry) 과정에서 발생하는 공통 로그를 처리하는 로거 클래스입니다.
 * <p>작업 실패 또는 재시도와 관련된 로그를 일관된 형식으로 출력합니다.</p>
 */
@Slf4j
public final class RetryLogger {

    private RetryLogger() {
        throw new AssertionError("Cannot instantiate logger class.");
    }

    /**
     * 작업(action) 실패로 인해 재시도를 수행할 때 현재 재시도 횟수를 포함하여 경고 로그를 출력합니다.
     *
     * @param action       수행하려는 작업 설명
     * @param currentRetry 현재 재시도 횟수
     * @param maxRetries   허용된 최대 재시도 횟수
     * @param e            실패 원인이 된 예외 객체
     */
    public static void logRetry(String action, int currentRetry, int maxRetries, Throwable e) {
        log.warn("Retry [{}/{}] - attempt {} failed: {}", action, currentRetry, maxRetries, e.getMessage(), e);
    }

    /**
     * 지정한 작업(action)이 최대 재시도 횟수(maxRetries) 이후에도 실패했을 때 경고 로그를 출력합니다.
     *
     * @param action     수행하려는 작업 설명
     * @param maxRetries 허용된 최대 재시도 횟수
     * @param e          실패 원인이 된 예외 객체
     */
    public static void logFailure(String action, int maxRetries, Throwable e) {
        log.warn("Failed to {} after {} retries: {}", action, maxRetries, e.getMessage(), e);
    }
}
