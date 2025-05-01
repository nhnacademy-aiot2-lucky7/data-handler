/*
package com.nhnacademy.database.influxdb.repository;

import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.nhnacademy.common.parser.dto.ParsingData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.Map;

@Slf4j
@SpringBootTest
@Import({InfluxDBConfig.class, WriteApi.class})
class InfluxDBRepositoryTest {

    @Autowired
    private InfluxDBConfig influxDBConfig;

    @Autowired
    private WriteApi writeApi;

    private ParsingData parsingData;

    @BeforeEach
    void setUp() {
        parsingData = new ParsingData(
                "24e124785c389010",
                "temperature",
                "온도",
                "hive",
                Map.of(
                        "time", 1745372230212L,
                        "value", 23.5
                )
        );
    }

    @DisplayName("데이터 저장 테스트")
    @Test
    void test() {

        Point point = Point
                .measurement("sensor_data")
                .addTag("sensor-id", parsingData.getData().get("value").toString())
                .addTag("data-type", parsingData.getDataType())
                .addTag("location", parsingData.getLocation())
                .addField("value", parsingData.getData().get("value").toString())
                .addField("data-type-kor", parsingData.getDataDesc())
                .time(parsingData.getInstant(), WritePrecision.MS);

        writeApi.writePoint(point);
        writeApi.flush();
    }
}*/
