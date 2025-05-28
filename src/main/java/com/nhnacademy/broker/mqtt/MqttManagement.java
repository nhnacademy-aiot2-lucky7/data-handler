package com.nhnacademy.broker.mqtt;

import com.nhnacademy.common.properties.MqttProperties;
import com.nhnacademy.sensor.service.SensorCacheService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public final class MqttManagement {

    private final SensorCacheService sensorInfoCache;

    private final MqttProperties properties;

    private final MqttCallback callback;

    private final MqttConnectOptions options;

    private IMqttClient mqttClient;

    private IMqttAsyncClient mqttAsyncClient;

    public MqttManagement(
            SensorCacheService sensorInfoCache,
            MqttProperties properties, MqttCallback callback,
            MqttConnectOptions options, MqttReconnectTrigger reconnectTrigger
    ) {
        this.sensorInfoCache = sensorInfoCache;
        this.properties = properties;
        this.callback = callback;
        this.options = options;
        reconnectTrigger.register(this); // 역참조 등록
    }

    /**
     * 설정 값에 따라, Sync 혹은 Async 모드로 실행합니다. <br>
     * <b>Default Option</b> - {@code ASYNC}
     */
    @PostConstruct
    public void init() throws MqttException {
        try {
            sensorInfoCache.getInitializationMono().block();
        } catch (Exception e) {
            log.error("센서 정보 캐싱 초기화 실패: {}", e.getMessage());
        }

        if (properties.isSyncMode()) {
            runSync();
        } else {
            runAsync();
        }
    }

    /**
     * Sync MQTT Client - 초기 설정 및 구동
     */
    private void runSync() throws MqttException {
        initMqttClient();
        mqttClient.setManualAcks(false);
        mqttClient.setCallback(callback);
        mqttClient.connect(options);
        mqttClient.subscribe(properties.getTopic(), properties.getQos());
        log.info("MQTT Sync Client Starting: {}", mqttClient.isConnected());
    }

    /**
     * Async MQTT Client - 초기 설정 및 구동
     */
    private void runAsync() throws MqttException {
        initMqttAsyncClient();
        mqttAsyncClient.setManualAcks(false);
        mqttAsyncClient.setCallback(callback);
        mqttAsyncClient.connect(options).waitForCompletion();
        mqttAsyncClient.subscribe(properties.getTopic(), properties.getQos()).waitForCompletion();
        log.info("MQTT Async Client Starting: {}", mqttAsyncClient.isConnected());
    }

    /**
     * Sync MQTT Client - 객체 초기화
     */
    private void initMqttClient() throws MqttException {
        String clientId = "%s_%d".formatted(
                properties.getClientId(),
                System.currentTimeMillis()
        );

        log.info("MQTT Sync Client ID: {}", clientId);
        mqttClient = new MqttClient(
                properties.getBrokerAddress(),
                clientId,
                new MemoryPersistence()
        );
    }

    /**
     * Async MQTT Client - 객체 초기화
     */
    private void initMqttAsyncClient() throws MqttException {
        String clientId = "%s_%d".formatted(
                properties.getClientId(),
                System.currentTimeMillis()
        );

        log.info("MQTT Async Client ID: {}", clientId);
        mqttAsyncClient = new MqttAsyncClient(
                properties.getBrokerAddress(),
                clientId,
                new MemoryPersistence()
        );
    }

    /**
     * 설정 값에 따라, Sync 혹은 Async 모드를 종료합니다. <br>
     * <b>Default Option</b> - {@code ASYNC}
     */
    public void close() {
        if (properties.isSyncMode()) {
            closeSync();
        } else {
            closeAsync();
        }
    }

    /**
     * Sync MQTT Client - 종료
     */
    private void closeSync() {
        if (Objects.nonNull(mqttClient) && mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();
                mqttClient.close();
                log.info("MQTT Sync Client: closed success");
            } catch (MqttException e) {
                log.warn("MQTT Sync Client: closed fail - {}", e.getMessage());
            }
        }
    }

    /**
     * Async MQTT Client - 종료
     */
    private void closeAsync() {
        if (Objects.nonNull(mqttAsyncClient) && mqttAsyncClient.isConnected()) {
            try {
                mqttAsyncClient.disconnect();
                mqttAsyncClient.close();
                log.info("MQTT Async Client: closed success");
            } catch (MqttException e) {
                log.warn("MQTT Async Client: closed fail - {}", e.getMessage());
            }
        }
    }
}
