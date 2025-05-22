package com.nhnacademy.sensor.service;

import com.nhnacademy.common.dto.SensorServiceErrorResponse;
import com.nhnacademy.common.exception.SensorServiceApiException;
import com.nhnacademy.common.parser.dto.ParsingData;
import com.nhnacademy.common.properties.SensorProperties;
import com.nhnacademy.sensor.SensorInfoCache;
import com.nhnacademy.sensor.SensorStatus;
import com.nhnacademy.sensor.dto.SensorDataIndex;
import com.nhnacademy.sensor.dto.SensorDataRegisterRequest;
import com.nhnacademy.sensor.dto.SensorInfo;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Set;

@Slf4j
@Component
public class SensorCacheService {

    private final WebClient webClient;

    private final SensorInfoCache cache;

    private final String path;

    @Getter
    private Mono<Void> initializationMono;

    public SensorCacheService(
            @Qualifier("sensorWebClient") WebClient webClient,
            SensorInfoCache cache,
            SensorProperties sensorProperties
    ) {
        this.webClient = webClient;
        this.cache = cache;
        this.path = sensorProperties.getSensorDataMappingPath();
    }

    @PostConstruct
    public void init() {
        initializationMono = webClient.get()
                .uri(path)
                .retrieve()
                .bodyToFlux(SensorDataIndex.class)
                .collectList()
                .doOnSuccess(data -> {
                    cache.refresh(Set.copyOf(data));
                    log.info("Sensor index initialized. Size: {}", data.size());
                })
                .doOnError(e -> {
                    log.error("Sensor index initialization failed: {}", e.getMessage(), e);
                })
                .then()
                .cache(); // only once
    }

    private Mono<Void> register(ParsingData parsingData) {
        SensorInfo info =
                new SensorInfo(
                        parsingData.getGatewayId(),
                        parsingData.getSensorId(),
                        parsingData.getLocation(),
                        parsingData.getSpot()
                );

        SensorDataRegisterRequest request =
                new SensorDataRegisterRequest(
                        info,
                        parsingData.getType(),
                        SensorStatus.PENDING.name()
                );

        return webClient.post()
                .uri(path)
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        res -> Mono.error(
                                new SensorServiceApiException()
                        )
                )
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        res -> res.bodyToMono(
                                        SensorServiceErrorResponse.class
                                )
                                .flatMap(err -> Mono.error(
                                                new SensorServiceApiException(err)
                                        )
                                )
                )
                .toBodilessEntity()
                .then();
    }

    public void checkAndRegister(ParsingData parsingData) {
        SensorDataIndex index = SensorDataIndex.of(
                parsingData.getGatewayId(),
                parsingData.getSensorId(),
                parsingData.getType()
        );

        if (cache.contains(index) || !cache.markTaskIfAbsent(index)) {
            return;
        }

        register(parsingData)
                .doOnSuccess(v -> {
                    cache.add(index);
                    cache.unmarkTask(index);
                    log.info("Register success: {}", index);
                })
                .doOnError(e -> {
                    cache.unmarkTask(index);
                    log.warn("Register failed for {}: {}", index, e.getMessage());
                })
                .block();
    }
}
