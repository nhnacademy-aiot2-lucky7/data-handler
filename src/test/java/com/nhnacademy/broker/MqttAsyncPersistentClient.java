package com.nhnacademy.broker;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Scanner;

/**
 * Eclipse Paho Client MQTT Client 테스트
 */
@Slf4j
public class MqttAsyncPersistentClient {

    private static final String BROKER = "tcp://115.94.72.197:1883";
    private static final String CLIENT_ID = "nhnacademy-team-luckyseven-live";
    private static final String TOPIC = "data/#";
    private static final int QOS = 1;

    public void start() {
        try {
            // MQTT 클라이언트 생성
            MqttAsyncClient client = new MqttAsyncClient(BROKER, CLIENT_ID, new MemoryPersistence());

            // 콜백 등록
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("❌ 연결 끊김: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    System.out.println("📩 메시지 수신됨 [Topic: " + topic + "]: " + message);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("📦 발행 완료");
                }
            });

            // 연결
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            System.out.println("🔌 브로커 연결 중...");
            client.connect(options).waitForCompletion();
            System.out.println("✅ 연결 완료");

            // 토픽 구독
            client.subscribe(TOPIC, QOS).waitForCompletion();
            System.out.println("📡 토픽 구독 중: " + TOPIC);

            // 메시지 발행 루프 (콘솔 입력 기반)
            Scanner scanner = new Scanner(System.in);
            String input;
            System.out.println("\n💬 메시지를 입력하세요 (종료하려면 'exit' 입력):");

            while (true) {
                System.out.print("> ");
                input = scanner.nextLine();

                if ("exit".equalsIgnoreCase(input)) break;

                MqttMessage message = new MqttMessage(input.getBytes());
                message.setQos(QOS);
                client.publish(TOPIC, message);
            }

            // 종료 처리
            // client.disconnect(); // .waitForCompletion();
            // client.close();
            // System.out.println("🔌 연결 종료됨. 프로그램 종료.");

        } catch (MqttException e) {
            log.error("{}", e.getMessage(), e);
        }
    }

    // 메인 메서드
    public static void main(String[] args) {
        new MqttAsyncPersistentClient().start();
    }
}
