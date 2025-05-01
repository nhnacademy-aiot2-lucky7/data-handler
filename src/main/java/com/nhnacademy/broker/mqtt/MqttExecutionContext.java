package com.nhnacademy.broker.mqtt;

import com.nhnacademy.common.thread.queue.InfluxDBQueue;
import com.nhnacademy.common.thread.queue.RuleEngineQueue;
import com.nhnacademy.influxdb.service.InfluxDBService;
import com.nhnacademy.rule.service.RuleEngineService;
import com.nhnacademy.type.util.DataTypeUtil;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class MqttExecutionContext {

    private final InfluxDBQueue influxDBQueue;

    private final InfluxDBService influxDBService;

    private final RuleEngineQueue ruleEngineQueue;

    private final RuleEngineService ruleEngineService;

    private final DataTypeUtil dataTypeUtil;

    public MqttExecutionContext(
            InfluxDBQueue influxDBQueue, InfluxDBService influxDBService,
            RuleEngineQueue ruleEngineQueue, RuleEngineService ruleEngineService,
            DataTypeUtil dataTypeUtil
    ) {
        this.influxDBQueue = influxDBQueue;
        this.influxDBService = influxDBService;
        this.ruleEngineQueue = ruleEngineQueue;
        this.ruleEngineService = ruleEngineService;
        this.dataTypeUtil = dataTypeUtil;
    }
}
