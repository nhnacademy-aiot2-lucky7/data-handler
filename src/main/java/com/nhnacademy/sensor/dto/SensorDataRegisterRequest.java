package com.nhnacademy.sensor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class SensorDataRegisterRequest {

    @JsonProperty("info")
    SensorInfo sensorInfo;

    @JsonProperty("type_en_name")
    String dataTypeEnName;

    @JsonProperty("sensor_status")
    String sensorStatus;
}
