package com.nhnacademy.broker.mqtt;

import com.nhnacademy.common.parser.dto.ParsingData;
import com.nhnacademy.common.thread.queue.impl.InfluxDBQueue;
import com.nhnacademy.common.thread.queue.impl.RuleEngineQueue;
import com.nhnacademy.influxdb.execute.InfluxDBExecute;
import com.nhnacademy.influxdb.service.InfluxDBService;
import com.nhnacademy.rule.execute.RuleEngineExecute;
import com.nhnacademy.rule.service.RuleEngineService;
import com.nhnacademy.sensor.service.SensorCacheService;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class MqttExecutionContext {

    private final SensorCacheService sensorInfoCache;

    private final InfluxDBQueue influxDBQueue;

    private final InfluxDBService influxDBService;

    private final RuleEngineQueue ruleEngineQueue;

    private final RuleEngineService ruleEngineService;

    public MqttExecutionContext(
            SensorCacheService sensorInfoCache,
            InfluxDBQueue influxDBQueue, InfluxDBService influxDBService,
            RuleEngineQueue ruleEngineQueue, RuleEngineService ruleEngineService
    ) {
        this.sensorInfoCache = sensorInfoCache;
        this.influxDBQueue = influxDBQueue;
        this.influxDBService = influxDBService;
        this.ruleEngineQueue = ruleEngineQueue;
        this.ruleEngineService = ruleEngineService;
    }

    public void checkAndRegister(ParsingData parsingData) {
        sensorInfoCache.checkAndRegister(parsingData);
    }

    public void influxDBTaskOffer(ParsingData parsingData) throws InterruptedException {
        influxDBQueue.offer(
                new InfluxDBExecute(influxDBService, parsingData)
        );
    }

    public void ruleEngineTaskOffer(ParsingData parsingData) throws InterruptedException {
        ruleEngineQueue.offer(
                new RuleEngineExecute(ruleEngineService, parsingData)
        );
    }
}
