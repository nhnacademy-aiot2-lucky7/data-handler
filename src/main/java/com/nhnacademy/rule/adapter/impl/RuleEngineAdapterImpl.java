package com.nhnacademy.rule.adapter.impl;

import com.nhnacademy.common.properties.RuleEngineProperties;
import com.nhnacademy.rule.adapter.RuleEngineAdapter;
import com.nhnacademy.rule.dto.RuleData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Slf4j
@Component
public class RuleEngineAdapterImpl implements RuleEngineAdapter {

    private final RuleEngineProperties properties;

    private final WebClient webClient;

    public RuleEngineAdapterImpl(RuleEngineProperties properties, WebClient webClient) {
        this.properties = properties;
        this.webClient = webClient;
    }

    @Override
    public boolean send(RuleData ruleData) {
        try {
            webClient.post()
                    .uri(properties.getPath())
                    .bodyValue(ruleData)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .subscribe();
            return true;
        } catch (WebClientException e) {
            log.warn("ruleEngine 문제 발생: {}", e.getMessage(), e);
            return false;
        }
    }
}
