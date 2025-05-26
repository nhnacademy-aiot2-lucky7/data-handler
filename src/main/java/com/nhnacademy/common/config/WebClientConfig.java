package com.nhnacademy.common.config;

import com.nhnacademy.common.properties.RuleEngineProperties;
import com.nhnacademy.common.properties.SensorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private final SensorProperties sensorProperties;

    private final RuleEngineProperties ruleEngineProperties;

    public WebClientConfig(
            SensorProperties sensorProperties,
            RuleEngineProperties ruleEngineProperties
    ) {
        this.sensorProperties = sensorProperties;
        this.ruleEngineProperties = ruleEngineProperties;
    }

    @Bean("sensorWebClient")
    WebClient sensorWebClient() {
        return WebClient.builder()
                .baseUrl(sensorProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean("ruleEngineWebClient")
    WebClient ruleEngineWebClient() {
        return WebClient.builder()
                .baseUrl(ruleEngineProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
