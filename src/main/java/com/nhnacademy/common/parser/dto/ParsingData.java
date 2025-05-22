package com.nhnacademy.common.parser.dto;

import lombok.Getter;

@Getter
public final class ParsingData {

    /**
     * 게이트웨이 ID
     */
    private final String gatewayId;

    /**
     * 센서 ID
     */
    private final String sensorId;

    /**
     * 센서가 설치된 공간 (Ex: 서버룸 P)
     */
    private final String location;

    /**
     * 센서가 설치된 상세위치 (Ex: 서버 컴퓨터 1행, E열)
     */
    private final String spot;

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

    public ParsingData(
            String gatewayId, String sensorId, String location,
            String spot, String type, Double value, Long timestamp
    ) {
        this.gatewayId = gatewayId;
        this.sensorId = sensorId;
        this.location = location;
        this.spot = spot;
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
    }
}
