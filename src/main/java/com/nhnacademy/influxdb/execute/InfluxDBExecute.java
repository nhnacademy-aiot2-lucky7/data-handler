package com.nhnacademy.influxdb.execute;

import com.nhnacademy.common.parser.dto.ParsingData;
import com.nhnacademy.common.thread.Executable;
import com.nhnacademy.influxdb.dto.InfluxData;
import com.nhnacademy.influxdb.service.InfluxDBService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class InfluxDBExecute implements Executable {

    private final InfluxDBService influxDBService;

    private final ParsingData parsingData;

    private int retryCount = 0;

    public InfluxDBExecute(InfluxDBService influxDBService, ParsingData parsingData) {
        this.influxDBService = influxDBService;
        this.parsingData = parsingData;
    }

    @Override
    public void execute() {
        InfluxData influxData = new InfluxData(
                parsingData.getGatewayId(),
                parsingData.getSensorId(),
                parsingData.getLocation(),
                parsingData.getType(),
                parsingData.getValue(),
                parsingData.getTimestamp()
        );
        influxDBService.sensorDataSave(influxData);
    }

    @Override
    public int getRetryCount() {
        return retryCount;
    }

    @Override
    public void incrementRetryCount() {
        retryCount++;
    }
}
