package com.nhnacademy.database.influxdb.repository.impl;

import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.nhnacademy.database.SensorData;
import com.nhnacademy.database.influxdb.repository.InfluxDBRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class InfluxDBRepositoryImpl implements InfluxDBRepository {

    private final WriteApi writeApi;

    public InfluxDBRepositoryImpl(WriteApi writeApi) {
        this.writeApi = writeApi;
    }

    @Override
    public void save(SensorData sensorData) {
        log.info("influxDB 저장: {}", sensorData);
        Object value = sensorData.getData().get("value");
        Point point = Point
                .measurement("sensor-data")
                .addTag("id", sensorData.getDeviceId())
                .addTag("type", sensorData.getDataType())
                .addTag("location", sensorData.getLocation())
                .addField("value", (value instanceof Integer) ? (int) value : (double) value)
                .addField("type-kor", sensorData.getDataDesc())
                .time(sensorData.getInstant(), WritePrecision.MS);
        writeApi.writePoint(point);
    }
}
