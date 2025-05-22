package com.nhnacademy.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "sensor")
public class SensorProperties {

    private String ipAddress = "localhost";

    private int port = 10238;

    private String sensorDataMappingPath = "/sensor-data-mappings";

    public String getBaseUrl() {
        return "http://%s:%d".formatted(ipAddress, port);
    }
}
