package com.nhnacademy.rule.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public final class RuleData {

    /**
     * 게이트웨이 ID
     */
    private final String gatewayId;

    /**
     * 센서 ID
     */
    private final String sensorId;

    /**
     * 센서가 측정하는 데이터 타입
     */
    private final String type;

    /**
     * 센서가 데이터를 측정한 값
     */
    private final Double value;

    /**
     * 센서가 데이터를 측정한 시간대
     */
    private final Long timestamp;

    @JsonCreator
    public RuleData(
            @JsonProperty("gateway-id") String gatewayId,
            @JsonProperty("sensor-id") String sensorId,
            @JsonProperty("type") String type,
            @JsonProperty("value") Double value,
            @JsonProperty("timestamp") Long timestamp
    ) {
        this.gatewayId = gatewayId;
        this.sensorId = sensorId;
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
    }
}
