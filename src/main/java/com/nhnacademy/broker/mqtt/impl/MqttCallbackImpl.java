package com.nhnacademy.broker.mqtt.impl;

import com.nhnacademy.broker.mqtt.MqttExecutionContext;
import com.nhnacademy.broker.mqtt.MqttReconnectTrigger;
import com.nhnacademy.broker.mqtt.execute.MqttExecute;
import com.nhnacademy.common.parser.DataParserResolver;
import com.nhnacademy.common.thread.queue.ParserQueue;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

/**
 * MQTT Client 콜백 설정을 커스텀한 클래스입니다.
 */
@Slf4j
@Component
public final class MqttCallbackImpl implements MqttCallback {

    private final MqttExecutionContext mqttExecutionContext;

    private final MqttReconnectTrigger reconnectTrigger;

    private final DataParserResolver parserResolver;

    private final ParserQueue parserQueue;

    public MqttCallbackImpl(
            MqttExecutionContext mqttExecutionContext,
            MqttReconnectTrigger reconnectTrigger,
            DataParserResolver parserResolver, ParserQueue parserQueue
    ) {
        this.mqttExecutionContext = mqttExecutionContext;
        this.reconnectTrigger = reconnectTrigger;
        this.parserResolver = parserResolver;
        this.parserQueue = parserQueue;
    }

    /**
     * MQTT Client 연결이 실패했을 경우
     */
    @Override
    public void connectionLost(Throwable cause) {
        log.error("MQTT Client connection error: {}", cause.getMessage());
        reconnectTrigger.triggerReconnect(); // 관리 객체에 직접 접근하지 않음
    }

    /**
     * MQTT Client 메세지를 받았을 경우
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        if (topic.contains("/status")) return;
        String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
        try {
            parserQueue.put(
                    new MqttExecute(
                            mqttExecutionContext,
                            parserResolver.getDataParser(payload),
                            topic,
                            payload
                    )
            );
        } catch (NoSuchElementException e) {
            log.warn("{}", e.getMessage(), e);
        }
    }

    /**
     * MQTT Client 메세지를 전송했을 경우
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}
