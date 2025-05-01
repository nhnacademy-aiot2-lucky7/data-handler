package com.nhnacademy.influxdb.repository.impl;

import com.influxdb.client.WriteApi;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import com.nhnacademy.influxdb.InfluxDBManagement;
import com.nhnacademy.influxdb.repository.InfluxDBRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class InfluxDBRepositoryImpl implements InfluxDBRepository {

    private final InfluxDBManagement management;

    private WriteApi writeApi;

    public InfluxDBRepositoryImpl(InfluxDBManagement management) {
        this.management = management;
    }

    @PostConstruct
    public void init() {
        this.writeApi = management.writeApi();
    }

    @Override
    public boolean save(Point point) {
        try {
            writeApi.writePoint(point);
            return true;
        } catch (InfluxException e) {
            log.warn("influxDB 재연결 시도: {}", e.getMessage(), e);
            init();
            return false;
        }
    }
}
