package com.nhnacademy.broker.mqtt.execute;

import com.nhnacademy.common.thread.Executable;
import com.nhnacademy.database.SensorData;
import com.nhnacademy.common.thread.queue.InfluxDBQueue;
import com.nhnacademy.common.thread.queue.RuleEngineQueue;
import com.nhnacademy.parser.DataParser;
import com.nhnacademy.type.util.DataTypeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

@Slf4j
public final class MqttExecute implements Executable {

    private final DataParser dataParser;

    private final DataTypeUtil dataTypeUtil;

    private final InfluxDBQueue influxDBQueue;

    private final RuleEngineQueue ruleEngineQueue;

    private final String topic;

    private final String payload;

    private String deviceId = "";

    private String dataType = "";

    private String location = "";

    public MqttExecute(
            DataParser dataParser, DataTypeUtil dataTypeUtil,
            InfluxDBQueue influxDBQueue, RuleEngineQueue ruleEngineQueue,
            String topic, String payload
    ) {
        this.dataParser = dataParser;
        this.dataTypeUtil = dataTypeUtil;
        this.influxDBQueue = influxDBQueue;
        this.ruleEngineQueue = ruleEngineQueue;
        this.topic = topic;
        this.payload = payload;
    }

    @Override
    public void execute() {
        Map<String, Object> parsed;
        try {
            parsed = dataParser.parsing(payload);
        } catch (IOException e) {
            /// TODO: 공통 예외처리 추가 예정...
            log.error("Parsing failed: {}", e.getMessage(), e);
            return;
        }

        topicParsing();
        String dataDesc = dataTypeUtil.get(dataType);
        if (dataDesc.equals("lora")) {
            return;
        }

        SensorData data = new SensorData(deviceId, dataType, dataDesc, location, parsed);
        influxDBQueue.put(data);
        ruleEngineQueue.put(data);
    }

    private void topicParsing() {
        String[] parsing = topic.split("/");
        for (int n = 0; n < parsing.length - 1; n++) {
            switch (parsing[n]) {
                case "p":
                    location = parsing[++n];
                    break;
                case "d":
                    deviceId = parsing[++n];
                    break;
                case "e":
                    dataType = parsing[++n];
                    break;
                default:
            }
        }
    }

    /**
     * 센서가 설치된 공간 및, 위치
     */
    // private String location;

    /**
     * 센서의 아이디
     */
    // private String deviceId;

    /**
     * 센서가 측정한 데이터의 이름
     */
    // private String dataName;
}
