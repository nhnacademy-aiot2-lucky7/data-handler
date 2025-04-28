package com.nhnacademy.rule;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class RuleEngine {

    private final WebClient webClient;

    public RuleEngine() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    public void sendData(DataDTO dataDTO) {
        webClient.post()
                .uri("/ruleEngine/data")
                .header("Content-Type", "application/json")
                .bodyValue(dataDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe(
                        unused -> System.out.println("전송 완료!"),
                        error -> System.err.println("에러 발생: " + error.getMessage())
                );
    }
}
