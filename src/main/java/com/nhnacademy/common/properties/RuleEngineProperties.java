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
     * Rule Engine의 REST API를 지정합니다.
     */
    private String baseUrl = "https://localhost:10245";

    /**
     * Rule Engine의 REST API Content URI를 지정합니다.
     */
    private String sendUri = "/ruleEngine/data";
}
