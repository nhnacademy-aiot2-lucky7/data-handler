package com.nhnacademy.data.common.config;

import com.nhnacademy.data.common.properties.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MqttConfig {

    private final MqttProperties mqttProperties;

    public MqttConfig(MqttProperties mqttProperties) {
        this.mqttProperties = mqttProperties;
    }

    /**
     * Create Sync MQTT Client
     */
    @Bean
    public IMqttClient iMqttClient() throws MqttException {
        return new MqttClient(
                mqttProperties.getBrokerAddress(),
                mqttProperties.getClientId(),
                new MemoryPersistence()
        );
    }

    /**
     * Create Async MQTT Client
     */
    @Bean
    public IMqttAsyncClient iMqttAsyncClient() throws MqttException {
        return new MqttAsyncClient(
                mqttProperties.getBrokerAddress(),
                mqttProperties.getClientId(),
                new MemoryPersistence()
        );
    }

    /**
     * MQTT Client의 연결, 구독, 발행 등 특정 작업의 성공/실패 로직을 커스텀합니다.
     */
    @Bean
    IMqttActionListener customActionListener() {
        return new IMqttActionListener() {
            /// 작업이 성공한 경우
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            /// 작업이 실패한 경우
            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        };
    }

    /**
     * MQTT 연결 설정을 커스텀합니다.
     */
    @Bean
    MqttConnectOptions customConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);    // 연결이 손실될 경우, 재연결 시도
        options.setCleanSession(true);          // 재연결 되었을 시, 이전의 session 연결을 clean 합니다.
        options.setKeepAliveInterval(30);
        return options;
    }
}
