package com.nhnacademy.rule;

import lombok.Value;

@Value
public class DataDTO {

    String deviceId;

    String dataType;

    String location;

    Object value;

    long time;
}
