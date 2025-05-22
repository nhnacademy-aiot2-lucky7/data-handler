package com.nhnacademy.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public final class SensorServiceErrorResponse {

    private final LocalDateTime timestamp;

    private final Integer status;

    private final String message;

    private final String path;

    @JsonCreator
    public SensorServiceErrorResponse(
            @JsonProperty("timestamp") LocalDateTime timestamp,
            @JsonProperty("status") Integer status,
            @JsonProperty("message") String message,
            @JsonProperty("path") String path
    ) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
    }
}
