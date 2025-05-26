package com.nhnacademy.common.exception;

import com.nhnacademy.common.dto.SensorServiceErrorResponse;

public class SensorServiceApiException extends RuntimeException {

    public SensorServiceApiException() {
        super("[API 호출 실패]: 서버 에러");
    }

    public SensorServiceApiException(SensorServiceErrorResponse sensorServiceErrorResponse) {
        super(
                "[API 호출 실패]: timestamp=%s, status=%d, message=%s, path=%s"
                        .formatted(
                                sensorServiceErrorResponse.getTimestamp(),
                                sensorServiceErrorResponse.getStatus(),
                                sensorServiceErrorResponse.getMessage(),
                                sensorServiceErrorResponse.getPath()
                        )
        );
    }
}
