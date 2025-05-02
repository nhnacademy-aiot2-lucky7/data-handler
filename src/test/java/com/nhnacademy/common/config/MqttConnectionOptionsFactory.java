package com.nhnacademy.common.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

@Deprecated
public class MqttConnectionOptionsFactory {

    private MqttConnectOptions customConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);    // 연결 끊기 시 자동 재연결
        options.setCleanSession(true);          // 클린 세션
        options.setKeepAliveInterval(60);       // 60초마다 Ping 메시지 전송
        options.setConnectionTimeout(10);       // 10초 연결 타임아웃
        options.setMaxInflight(50);             // 최대 50개의 메시지를 비행 중으로 허용

        // 사용자 인증 설정
        options.setUserName("username");
        options.setPassword("password".toCharArray());

        // SSL/TLS 연결 설정
        // SSLSocketFactory sslSocketFactory = ...;
        // options.setSocketFactory(sslSocketFactory);

        return options;
    }
}
