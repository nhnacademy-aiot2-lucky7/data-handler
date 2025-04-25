package com.nhnacademy.data.broker.mqtt;

import com.nhnacademy.data.broker.mqtt.execute.MqttParser;
import com.nhnacademy.data.common.thread.queue.InfluxDBQueue;
import com.nhnacademy.data.common.thread.queue.ParserQueue;
import com.nhnacademy.data.common.thread.queue.RuleEngineQueue;
import com.nhnacademy.data.parser.DataParser;
import com.nhnacademy.data.parser.DataParserResolver;
import com.nhnacademy.data.type.util.DataTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * MQTT Client 콜백 설정을 커스텀한 클래스입니다.
 */
@Slf4j
@Component
public final class CustomMqttCallback implements MqttCallback {

    private final DataParserResolver parserResolver;

    private final DataTypeUtil dataTypeUtil;

    private final ParserQueue parserQueue;

    private final InfluxDBQueue influxDBQueue;

    private final RuleEngineQueue ruleEngineQueue;

    public CustomMqttCallback(
            DataParserResolver parserResolver, DataTypeUtil dataTypeUtil,
            ParserQueue parserQueue, InfluxDBQueue influxDBQueue, RuleEngineQueue ruleEngineQueue
    ) {
        this.parserResolver = parserResolver;
        this.dataTypeUtil = dataTypeUtil;
        this.parserQueue = parserQueue;
        this.influxDBQueue = influxDBQueue;
        this.ruleEngineQueue = ruleEngineQueue;
    }

    /**
     * MQTT Client 연결이 실패했을 경우
     */
    @Override
    public void connectionLost(Throwable cause) {
        log.error("MQTT Client connection error: {}", cause.getMessage());
    }

    /**
     * MQTT Client 메세지를 받았을 경우
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if (topic.contains("/status")) {
            return;
        }

        String data = new String(message.getPayload(), StandardCharsets.UTF_8);
        DataParser dataParser = parserResolver.getDataParser(data);
        parserQueue.put(new MqttParser(
                dataParser, dataTypeUtil, influxDBQueue, ruleEngineQueue, topic, data
        ));
    }

    /**
     * MQTT Client 메세지를 전송했을 경우
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
