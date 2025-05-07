package com.nhnacademy.broker.mqtt.execute;

import com.nhnacademy.broker.mqtt.MqttExecutionContext;
import com.nhnacademy.common.parser.DataParser;
import com.nhnacademy.common.parser.dto.ParsingData;
import com.nhnacademy.common.thread.Executable;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Slf4j
public final class MqttExecute implements Executable {

    /**
     * 반올림에 사용하는 배수라는 점을 직관적으로 표현합니다.
     */
    private static final double DEFAULT_ROUNDING_FACTOR = 100.0;

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
        } catch (Exception e) {
            log.error("Parsing failed(topic: {} / payload: {}): {}", topic, payload, e.getMessage(), e);
            return;
        }
        /// TODO: 지원되지 않는 형식의 value 구조일 경우를 처리할 로직을 추후에 추가...
        if (Objects.isNull(value)) return;
        topicParsing();

        ParsingData parsingData = new ParsingData(
                gatewayId, sensorId, location,
                spot, type, value, timestamp
        );

        try {
            context.influxDBTaskOffer(parsingData);
            context.ruleEngineTaskOffer(parsingData);
        } catch (InterruptedException e) {
            log.error("MQTT Execute: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }

    private void topicParsing() {
        String[] parsing = topic.split("/");
        for (int n = 0; n < parsing.length - 1; n++) {
            switch (parsing[n]) {
                case "p":
                    location = parsing[++n];
                    break;
                case "d":
                    sensorId = parsing[++n];
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

        Object rawValue = parsing.get("value");
        if (rawValue instanceof Number number) {
            value = Math.round(number.doubleValue() * DEFAULT_ROUNDING_FACTOR) / DEFAULT_ROUNDING_FACTOR;
        } else if (rawValue instanceof String string) {
            value = Math.round(Double.parseDouble(string) * DEFAULT_ROUNDING_FACTOR) / DEFAULT_ROUNDING_FACTOR;
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
