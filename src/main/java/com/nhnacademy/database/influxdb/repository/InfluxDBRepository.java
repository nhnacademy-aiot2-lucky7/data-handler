package com.nhnacademy.database.influxdb.repository;

import com.nhnacademy.database.SensorData;

public interface InfluxDBRepository {

    void save(SensorData sensorData);
}
