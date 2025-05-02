package com.nhnacademy.broker.mqtt.execute;

import com.nhnacademy.broker.mqtt.MqttExecutionContext;
import com.nhnacademy.common.parser.DataParser;
import com.nhnacademy.common.parser.dto.ParsingData;
import com.nhnacademy.common.thread.Executable;
import com.nhnacademy.influxdb.execute.InfluxDBExecute;
import com.nhnacademy.rule.execute.RuleEngineExecute;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Slf4j
public final class MqttExecute implements Executable {

    private final MqttExecutionContext context;

    private final DataParser dataParser;

    private final String topic;

    private final String payload;

    private String gatewayId = "null";

    private String sensorId = "null";

    private String location = "null";

    private String spot = "null";

    private String type = "null";

    private Double value = null;

    private Long timestamp = null;

    public MqttExecute(
            MqttExecutionContext context, DataParser dataParser,
            String topic, String payload
    ) {
        this.context = context;
        this.dataParser = dataParser;
        this.topic = topic;
        this.payload = payload;
    }

    @Override
    public void execute() {
        try {
            payloadParsing();
        } catch (IOException e) {
            log.error("Parsing failed: {}", e.getMessage(), e);
            return;
        }
        topicParsing();

        ParsingData parsingData = new ParsingData(
                gatewayId, sensorId, location,
                spot, type, value, timestamp
        );

        context.getRuleEngineQueue().put(
                new RuleEngineExecute(context.getRuleEngineService(), parsingData)
        );
        context.getInfluxDBQueue().put(
                new InfluxDBExecute(context.getInfluxDBService(), parsingData)
        );
    }

    private void topicParsing() {
        String[] parsing = topic.split("/");
        for (int n = 0; n < parsing.length - 1; n++) {
            switch (parsing[n]) {
                case "p":
                    location = parsing[++n];
                    break;
                case "d":
                    String id = parsing[++n];
                    // gatewayId = id;
                    sensorId = id;
                    break;
                case "e":
                    type = parsing[++n];
                    break;
                default:
            }
        }
    }

    private void payloadParsing() throws IOException {
        Map<String, Object> parsing = dataParser.parsing(payload);

        // 소수점 2자리까지만
        // Math.ceil();
        Object rawValue = parsing.get("value");
        if (rawValue instanceof Number number) {
            value = number.doubleValue();
        } else if (rawValue instanceof String string) {
            value = Double.parseDouble(string);
        }

        Object rawTime = parsing.get("time");
        if (rawTime instanceof Number number) {
            timestamp = number.longValue();
        } else if (rawTime instanceof String string) {
            try {
                timestamp = Long.parseLong(string);
            } catch (NumberFormatException ignored) {
                timestamp = Instant.now().getEpochSecond();
            }
        }
    }
}
