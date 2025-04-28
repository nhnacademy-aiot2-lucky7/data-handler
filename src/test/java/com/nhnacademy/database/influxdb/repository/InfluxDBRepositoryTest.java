package com.nhnacademy.database.influxdb.repository;

import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.nhnacademy.common.config.InfluxDBConfig;
import com.nhnacademy.database.SensorData;
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

    private SensorData sensorData;

    @BeforeEach
    void setUp() {
        sensorData = new SensorData(
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
                .addTag("sensor-id", sensorData.getData().get("value").toString())
                .addTag("data-type", sensorData.getDataType())
                .addTag("location", sensorData.getLocation())
                .addField("value", sensorData.getData().get("value").toString())
                .addField("data-type-kor", sensorData.getDataDesc())
                .time(sensorData.getInstant(), WritePrecision.MS);

        writeApi.writePoint(point);
        writeApi.flush();
    }
}