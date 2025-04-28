package com.nhnacademy.common.config;

import com.influxdb.client.InfluxDBClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@EnableScheduling
public class InfluxDBPing {

    private final InfluxDBClient influxDBClient;

    public InfluxDBPing(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    @Scheduled(fixedRate = 5 * 60 * 1000) // 5분마다
    public void keepAlive() {
        if (Objects.nonNull(influxDBClient)) {
            influxDBClient.ping();
        }
    }
}
