package com.nhnacademy.common.properties;

import com.nhnacademy.common.enums.CoreBrokerTransportMode;
import com.nhnacademy.common.enums.TransferMode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "core-broker-receiver")
public final class CoreBrokerProperties {

    private TransferMode receiveMode = TransferMode.ASYNC;

    private CoreBrokerTransportMode transportMode = CoreBrokerTransportMode.TCP;

    private String address = "localhost";

    private int port = 1883;

    private String clientId = "core-broker-receiver";

    private String topic = "team1_data/#";

    private Integer qos = 1;

    public boolean isSecure() {
        return transportMode.isSecure();
    }
}
