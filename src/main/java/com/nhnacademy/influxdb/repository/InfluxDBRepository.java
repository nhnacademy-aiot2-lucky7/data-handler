package com.nhnacademy.influxdb.repository;

import com.influxdb.client.write.Point;

public interface InfluxDBRepository {

    boolean save(Point point);
}
