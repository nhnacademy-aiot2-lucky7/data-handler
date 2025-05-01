package com.nhnacademy.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rule-engine")
public final class RuleEngineProperties {

    /**
     * start data entry with Rule Engine
     */
    private boolean start = false;

    /**
     * Rule Engine Service에 할당된 Ip address를 지정합니다.
     */
    private String ipAddress = "localhost";

    /**
     * Rule Engine Service에 할당된 Port number를 지정합니다.
     */
    private int port = 10245;

    /**
     * Rule Engine Service에서 Parsing 데이터를 받을 수 있는 REST Controller Path를 지정합니다.
     */
    private String path = "/ruleEngine/data";

    public String getBaseUrl() {
        return "https://%s:%d".formatted(ipAddress, port);
    }
}
