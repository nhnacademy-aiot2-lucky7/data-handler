package com.nhnacademy.common.config;

import com.influxdb.LogLevel;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.InfluxDBClientOptions;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;
import com.nhnacademy.common.properties.InfluxDBProperties;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Configuration
public class InfluxDBConfig {

    private final InfluxDBProperties influxDBProperties;

    public InfluxDBConfig(InfluxDBProperties influxDBProperties) {
        this.influxDBProperties = influxDBProperties;
    }

    // 앱 종료 시 flush를 위한 설정
    @Bean(destroyMethod = "close")
    InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(customInfluxDBClientOptions());
    }

    /*@Bean
    WriteApiBlocking writeApiBlocking(InfluxDBClient influxDBClient) {
        return influxDBClient.getWriteApiBlocking();
    }*/

    @Bean
    WriteApi writeApi(InfluxDBClient influxDBClient) {
        // WriteApi writeApi = influxDBClient.makeWriteApi(customWriteOptions());
        // Optional: 성공/실패 콜백 등록
        /*writeApi.listenEvents(WriteSuccessEvent.class, event ->
                System.out.println("✅ Data written successfully")
        );
        writeApi.listenEvents(WriteErrorEvent.class, error ->
                System.err.println("❌ Failed to write: " + error.getThrowable().getMessage())
        );*/
        return influxDBClient.makeWriteApi(customWriteOptions());
    }

    private InfluxDBClientOptions customInfluxDBClientOptions() {
        return InfluxDBClientOptions.builder()
                .url(influxDBProperties.getUrl())
                .okHttpClient(customOkHttpClientBuilder())
                .org(influxDBProperties.getOrg())
                .bucket(influxDBProperties.getBucket())
                .authenticateToken(influxDBProperties.getToken())
                .logLevel(LogLevel.NONE)
                .build();
    }

    private OkHttpClient.Builder customOkHttpClientBuilder() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectionPool(customConnectionPool());
    }

    private ConnectionPool customConnectionPool() {
        return new ConnectionPool(2, 365, TimeUnit.DAYS);
    }

    private WriteOptions customWriteOptions() {
        return WriteOptions.builder()
                .batchSize(100)  // 100개 모아서 한번에 전송
                .flushInterval(1000) // 1초마다 flush
                .writeScheduler(Schedulers.from(Executors.newFixedThreadPool(2))) // 직접 스레드풀 설정
                .build();
    }
}
