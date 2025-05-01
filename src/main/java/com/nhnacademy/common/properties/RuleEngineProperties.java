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
     * Rule Engine의 REST API의 주소를 지정합니다.
     */
    private String baseUrl = "https://localhost:10245";

    /**
     * Rule Engine이 Parsing 데이터를 받을 수 있는 REST Controller Path를 지정합니다.
     */
    private String path = "/ruleEngine/data";
}
