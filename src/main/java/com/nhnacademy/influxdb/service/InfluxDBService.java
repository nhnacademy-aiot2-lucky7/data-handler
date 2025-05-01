package com.nhnacademy.influxdb.service;

import com.nhnacademy.influxdb.dto.InfluxData;

public interface InfluxDBService {

    void sensorDataSave(InfluxData influxData);
}
