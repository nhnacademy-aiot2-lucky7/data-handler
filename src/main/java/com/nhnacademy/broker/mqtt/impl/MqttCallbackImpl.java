package com.nhnacademy.broker.mqtt.impl;

import com.nhnacademy.broker.mqtt.MqttExecutionContext;
import com.nhnacademy.broker.mqtt.MqttReconnectTrigger;
import com.nhnacademy.broker.mqtt.execute.MqttExecute;
import com.nhnacademy.common.parser.DataParserResolver;
import com.nhnacademy.common.thread.queue.impl.ParserQueue;
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

    private final MqttReconnectTrigger mqttReconnectTrigger;

    private final DataParserResolver parserResolver;

    private final ParserQueue parserQueue;

    public MqttCallbackImpl(
            MqttExecutionContext mqttExecutionContext,
            MqttReconnectTrigger mqttReconnectTrigger,
            DataParserResolver parserResolver, ParserQueue parserQueue
    ) {
        this.mqttExecutionContext = mqttExecutionContext;
        this.mqttReconnectTrigger = mqttReconnectTrigger;
        this.parserResolver = parserResolver;
        this.parserQueue = parserQueue;
    }

    /**
     * MQTT Client 연결이 실패했을 경우
     */
    @Override
    public void connectionLost(Throwable cause) {
        log.error("MQTT Client connection error: {}", cause.getMessage(), cause);
        mqttReconnectTrigger.reconnection();
    }

    /**
     * MQTT Client 메세지를 받았을 경우
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        if (topic.contains("/status")) return;
        String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
        try {
            parserQueue.offer(
                    new MqttExecute(
                            mqttExecutionContext,
                            parserResolver.getDataParser(payload),
                            topic,
                            payload
                    )
            );
        } catch (NoSuchElementException e) {
            log.warn("MQTT Message Arrived: {}", e.getMessage(), e);
        } catch (InterruptedException e) {
            log.error("MQTT Message Arrived: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * MQTT Client 메세지를 전송했을 경우
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}
