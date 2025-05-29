package com.nhnacademy.broker.core.dto;

import com.nhnacademy.broker.mqtt.dto.MqttBroker;
import com.nhnacademy.common.enums.IoTProtocol;
import com.nhnacademy.common.properties.CoreBrokerProperties;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MqttCoreBroker extends MqttBroker {

    public MqttCoreBroker(CoreBrokerProperties properties) {
        super(
                properties.getAddress(),
                properties.getPort(),
                properties.isSecure() ? IoTProtocol.MQTT_TLS : IoTProtocol.MQTT,
                properties.getClientId(),
                properties.getTopic(),
                properties.getQos(),
                properties.getReceiveMode()
        );
    }
}
