package com.nhnacademy.broker.mqtt;

import com.nhnacademy.common.properties.MqttProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class MqttReceiver {

    private final MqttProperties mqttProperties;

    private final IMqttClient mqttClient;

    private final IMqttAsyncClient mqttAsyncClient;

    private final MqttCallback callback;

    private final MqttConnectOptions options;

    public MqttReceiver(
            MqttProperties mqttProperties,
            IMqttClient mqttClient, IMqttAsyncClient mqttAsyncClient,
            MqttCallback callback, MqttConnectOptions options
    ) {
        this.mqttProperties = mqttProperties;
        this.mqttClient = mqttClient;
        this.mqttAsyncClient = mqttAsyncClient;
        this.callback = callback;
        this.options = options;
    }

    /**
     * 설정 값에 따라, Sync 혹은 Async 모드로 실행합니다. <br>
     * <b>Default Option</b> - {@code SYNC}
     */
    @PostConstruct
    private void init() throws MqttException {
        if (mqttProperties.isSyncMode()) {
            runSync();
            closeAsync();
        } else {
            runAsync();
            closeSync();
        }
    }

    /**
     * Sync MQTT Client - 초기 설정 및 구동
     */
    private void runSync() throws MqttException {
        mqttClient.setCallback(callback);
        mqttClient.connect(options);
        mqttClient.subscribe("data/#", 1);
        log.info("MQTT Sync Client Starting: {}", mqttClient.isConnected());
    }

    /**
     * Async MQTT Client - 초기 설정 및 구동
     */
    private void runAsync() throws MqttException {
        mqttAsyncClient.setCallback(callback);
        mqttAsyncClient.connect(options).waitForCompletion();
        mqttAsyncClient.subscribe("data/#", 1).waitForCompletion();
        log.info("MQTT Async Client Starting: {}", mqttAsyncClient.isConnected());
    }

    /**
     * Spring Context가 내려가기 전에 자동 호출
     */
    @PreDestroy
    public void close() {
        if (mqttProperties.isSyncMode()) {
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
                log.warn("❌ MQTT Sync 종료 중 예외 발생: {}", e.getMessage());
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
                log.info("✅ MQTT Async 클라이언트 정상 종료");
            } catch (MqttException e) {
                log.warn("❌ MQTT Async 종료 중 예외 발생: {}", e.getMessage());
            }
        }
    }
}
