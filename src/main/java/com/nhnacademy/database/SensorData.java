package com.nhnacademy.database;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.Map;

@Getter
@ToString
public final class SensorData {

    private final String deviceId;

    private final String dataType;

    private final String dataDesc;

    private final String location;

    private final Map<String, Object> data;

    public SensorData(
            String deviceId, String dataType, String dataDesc,
            String location, Map<String, Object> data
    ) {
        this.deviceId = deviceId;
        this.dataType = dataType;
        this.dataDesc = dataDesc;
        this.location = location;
        this.data = data;
    }

    /**
     * instanceof 패턴 매칭 방식으로 구현
     *
     * @return
     */
    public Instant getInstant() {
        Object time = data.get("time");
        if (time instanceof Number number) {
            return Instant.ofEpochMilli(number.longValue());
        } else if (time instanceof String string) {
            try {
                return Instant.ofEpochMilli(Long.parseLong(string));
            } catch (NumberFormatException ignored) {
                // LocalDateTime 형식 혹은, 이외의 양식들인 경우...
            }
        }
        return Instant.now();
    }
}
