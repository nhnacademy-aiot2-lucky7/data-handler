package com.nhnacademy.broker.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public final class MqttReconnectTrigger {

    private MqttManagement mqttManagement;

    private AtomicBoolean reconnecting;

    public void register(MqttManagement mqttManagement) {
        this.mqttManagement = mqttManagement;
        this.reconnecting = new AtomicBoolean(false);
    }

    public void reconnection() {
        if (mqttManagement == null) {
            log.warn("MQTT 컴포넌트 구성 오류, 'MqttManagement'");
            return;
        }

        if (!reconnecting.compareAndSet(false, true)) {
            try {
                log.info("이미 재연결을 시도 중입니다.");
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return;
        }

        new Thread(() -> {
            try {
                run();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            } finally {
                reconnecting.set(false);
            }
        }, "mqtt-reconnect-thread").start();
    }

    private void run() throws InterruptedException {
        while (true) {
            try {
                mqttManagement.close();
                mqttManagement.init();
                log.info("MQTT 재연결 성공");
                break;
            } catch (MqttException e) {
                log.warn("MQTT 재연결 실패: {}", e.getMessage());
            }
            Thread.sleep(2000);
        }
    }
}
