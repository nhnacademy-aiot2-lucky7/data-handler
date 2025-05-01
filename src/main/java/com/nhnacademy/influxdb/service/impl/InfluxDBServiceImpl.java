package com.nhnacademy.influxdb.service.impl;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.nhnacademy.influxdb.dto.InfluxData;
import com.nhnacademy.influxdb.repository.InfluxDBRepository;
import com.nhnacademy.influxdb.service.InfluxDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InfluxDBServiceImpl implements InfluxDBService {

    private final InfluxDBRepository influxDBRepository;

    public InfluxDBServiceImpl(InfluxDBRepository influxDBRepository) {
        this.influxDBRepository = influxDBRepository;
    }

    public void sensorDataSave(InfluxData influxData) {
        Point point = Point
                .measurement("sensor-data")
                .addTag("gateway-id", influxData.getGatewayId())
                .addTag("sensor-id", influxData.getSensorId())
                .addField(influxData.getType(), influxData.getValue())
                .time(influxData.getTimestamp(), WritePrecision.MS);

        if (!influxDBRepository.save(point)) {
            /// TODO: Thread 영역에서 실패한 작업을 다시 시도할 수 있도록 throws 발생
            /// 추후 커스텀 Exception을 제작할 예정...
            throw new RuntimeException();
        }
    }
}
