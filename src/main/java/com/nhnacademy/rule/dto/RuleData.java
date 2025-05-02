package com.nhnacademy.rule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class RuleData {

    /**
     * 게이트웨이 ID
     */
    @JsonProperty("gateway_id")
    private final String gatewayId;

    /**
     * 센서 ID
     */
    @JsonProperty("sensor_id")
    private final String sensorId;

    /**
     * 센서가 측정하는 데이터 타입
     */
    @JsonProperty("type")
    private final String type;

    /**
     * 센서가 데이터를 측정한 값
     */
    @JsonProperty("value")
    private final Double value;

    /**
     * 센서가 데이터를 측정한 시간대
     */
    @JsonProperty("timestamp")
    private final Long timestamp;
}
