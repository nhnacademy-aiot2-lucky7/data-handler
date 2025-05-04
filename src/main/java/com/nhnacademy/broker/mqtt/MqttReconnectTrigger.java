package com.nhnacademy.broker.mqtt;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public final class MqttReconnectTrigger {

    private MqttManagement mqttManagement;

    public void register(MqttManagement mqttManagement) {
        this.mqttManagement = mqttManagement;
    }

    public void triggerReconnect() {
        if (Objects.nonNull(mqttManagement)) {
            mqttManagement.reconnect();
        }
    }
}
