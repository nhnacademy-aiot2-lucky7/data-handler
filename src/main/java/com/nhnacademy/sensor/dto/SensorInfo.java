package com.nhnacademy.sensor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class SensorInfo {

    @JsonProperty("gateway_id")
    String gatewayId;

    @JsonProperty("sensor_id")
    String sensorId;

    @JsonProperty("sensor_location")
    String sensorLocation;

    @JsonProperty("sensor_spot")
    String sensorSpot;
}
