package com.nhnacademy.broker.mqtt;

import org.springframework.stereotype.Component;

@Component
public final class MqttReconnectTrigger {

    private MqttManagement mqttManagement;

    public void register(MqttManagement mqttManagement) {
        this.mqttManagement = mqttManagement;
    }

    public void triggerReconnect() {
        if (mqttManagement != null) {
            mqttManagement.reconnect();
        }
    }
}
