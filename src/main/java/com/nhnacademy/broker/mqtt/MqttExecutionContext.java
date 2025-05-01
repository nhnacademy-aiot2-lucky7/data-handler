package com.nhnacademy.broker.mqtt;

import com.nhnacademy.common.thread.queue.InfluxDBQueue;
import com.nhnacademy.common.thread.queue.RuleEngineQueue;
import com.nhnacademy.influxdb.service.InfluxDBService;
import com.nhnacademy.rule.service.RuleEngineService;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class MqttExecutionContext {

    private final InfluxDBQueue influxDBQueue;

    private final InfluxDBService influxDBService;

    private final RuleEngineQueue ruleEngineQueue;

    private final RuleEngineService ruleEngineService;

    public MqttExecutionContext(
            InfluxDBQueue influxDBQueue, InfluxDBService influxDBService,
            RuleEngineQueue ruleEngineQueue, RuleEngineService ruleEngineService
    ) {
        this.influxDBQueue = influxDBQueue;
        this.influxDBService = influxDBService;
        this.ruleEngineQueue = ruleEngineQueue;
        this.ruleEngineService = ruleEngineService;
    }
}
