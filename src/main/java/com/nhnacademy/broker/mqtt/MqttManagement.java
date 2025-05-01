package com.nhnacademy.broker.mqtt;

import com.nhnacademy.common.properties.MqttProperties;
import jakarta.annotation.PostConstruct;
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
public final class MqttManagement {

    private final MqttProperties properties;

    private final MqttCallback callback;

    private final MqttConnectOptions options;

    private final IMqttClient mqttClient;

    private final IMqttAsyncClient mqttAsyncClient;

    public MqttManagement(
            MqttProperties properties,
            MqttCallback callback, MqttConnectOptions options,
            IMqttClient mqttClient, IMqttAsyncClient mqttAsyncClient
    ) {
        this.properties = properties;
        this.callback = callback;
        this.options = options;
        this.mqttClient = mqttClient;
        this.mqttAsyncClient = mqttAsyncClient;
    }

    /**
     * 설정 값에 따라, Sync 혹은 Async 모드로 실행합니다. <br>
     * <b>Default Option</b> - {@code SYNC}
     */
    @PostConstruct
    public void init() throws MqttException {
        if (properties.isSyncMode()) {
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
        mqttClient.subscribe(properties.getTopic(), properties.getQos());
        log.info("MQTT Sync Client Starting: {}", mqttClient.isConnected());
    }

    /**
     * Async MQTT Client - 초기 설정 및 구동
     */
    private void runAsync() throws MqttException {
        mqttAsyncClient.setCallback(callback);
        mqttAsyncClient.connect(options).waitForCompletion();
        mqttAsyncClient.subscribe(properties.getTopic(), properties.getQos()).waitForCompletion();
        log.info("MQTT Async Client Starting: {}", mqttAsyncClient.isConnected());
    }

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
