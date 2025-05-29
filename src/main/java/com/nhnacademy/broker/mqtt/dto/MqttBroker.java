package com.nhnacademy.broker.mqtt.dto;

import com.nhnacademy.common.enums.IoTProtocol;
import com.nhnacademy.common.enums.TransferMode;
import lombok.Getter;

@Getter
public abstract class MqttBroker {

    private final String address;

    private final int port;

    private final IoTProtocol protocol;

    private final String clientId;

    private final String topic;

    private final int qos;

    private final TransferMode receiveMode;

    protected MqttBroker(
            String address, int port, IoTProtocol protocol,
            String clientId, String topic, int qos,
            TransferMode receiveMode
    ) {
        this.address = address;
        this.port = port;
        this.protocol = protocol;
        this.clientId = clientId;
        this.topic = topic;
        this.qos = qos;
        this.receiveMode = receiveMode;
    }

    public String getServerURI() {
        return "%s://%s:%d".formatted(
                getProtocolScheme(),
                address,
                port
        );
    }

    public String getBuildClientIdWithTimestamp() {
        return "%s_%d".formatted(
                clientId,
                System.currentTimeMillis()
        );
    }

    public boolean isSyncMode() {
        return receiveMode.equals(TransferMode.SYNC);
    }

    public boolean isAsyncMode() {
        return receiveMode.equals(TransferMode.ASYNC);
    }

    private String getProtocolScheme() {
        return protocol.isSecure() ? "ssl" : "tcp";
    }
}
