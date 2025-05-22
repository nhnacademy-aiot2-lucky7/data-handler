package com.nhnacademy.sensor.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public final class SensorDataIndex {

    private final String gatewayId;

    private final String sensorId;

    private final String dataTypeEnName;

    @JsonCreator
    public SensorDataIndex(
            @JsonProperty("gateway_id") String gatewayId,
            @JsonProperty("sensor_id") String sensorId,
            @JsonProperty("data_type_en_name") String dataTypeEnName
    ) {
        this.gatewayId = gatewayId;
        this.sensorId = sensorId;
        this.dataTypeEnName = dataTypeEnName;
    }

    public static SensorDataIndex of(String gatewayId, String sensorId, String dataTypeEnName) {
        return new SensorDataIndex(
                gatewayId,
                sensorId,
                dataTypeEnName
        );
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SensorDataIndex that)) return false;
        return Objects.equals(gatewayId, that.gatewayId)
                && Objects.equals(sensorId, that.sensorId)
                && Objects.equals(dataTypeEnName, that.dataTypeEnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gatewayId, sensorId, dataTypeEnName);
    }
}
