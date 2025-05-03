package com.nhnacademy.broker.mqtt;

import com.nhnacademy.common.properties.MqttProperties;
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
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public final class MqttManagement {

    private final MqttProperties properties;

    private final MqttCallback callback;

    private final MqttConnectOptions options;

    private IMqttClient mqttClient;

    private IMqttAsyncClient mqttAsyncClient;

    public MqttManagement(
            MqttProperties properties, MqttCallback callback,
            MqttConnectOptions options, MqttReconnectTrigger reconnectTrigger
    ) {
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
        mqttClient = new MqttClient(
                properties.getBrokerAddress(),
                properties.getClientId(),
                new MemoryPersistence()
        );
    }

    /**
     * Async MQTT Client - 객체 초기화
     */
    private void initMqttAsyncClient() throws MqttException {
        mqttAsyncClient = new MqttAsyncClient(
                properties.getBrokerAddress(),
                properties.getClientId(),
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

    private final AtomicBoolean reconnecting = new AtomicBoolean(false);

    /// TODO: 긴급 추가
    public void reconnect() {
        if (!reconnecting.compareAndSet(false, true)) {
            log.info("이미 재연결 중입니다.");
            return;
        }

        new Thread(() -> {
            try {
                while (true) {
                    try {
                        close();
                        if (properties.isSyncMode()) {
                            runSync();
                        } else {
                            runAsync();
                        }
                        log.info("✅ MQTT 재연결 성공");
                        break;
                    } catch (MqttException e) {
                        log.warn("🔁 MQTT 재연결 실패: {}", e.getMessage());
                    }
                    Thread.sleep(5000);
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            } finally {
                reconnecting.set(false);
            }
        }, "mqtt-reconnect-thread").start();
    }
}
