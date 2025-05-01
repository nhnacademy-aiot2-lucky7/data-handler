package com.nhnacademy.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "mqtt")
public final class MqttProperties {

    private TransferMode receiveMode = TransferMode.ASYNC;

    private String brokerAddress = "tcp://localhost:1883";

    private String clientId = "mqtt-client-paho";

    private String topic = "data/#";

    private Integer qos = 1;

    public boolean isSyncMode() {
        return receiveMode.equals(TransferMode.SYNC);
    }

    public boolean isAsyncMode() {
        return receiveMode.equals(TransferMode.ASYNC);
    }
}
