package com.nhnacademy.influxdb;

import com.influxdb.LogLevel;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.InfluxDBClientOptions;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;
import com.nhnacademy.common.properties.InfluxDBProperties;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jakarta.annotation.PostConstruct;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public final class InfluxDBManagement {

    private final InfluxDBProperties influxDBProperties;

    private InfluxDBClient influxDBClient;

    private WriteApi writeApi;

    public InfluxDBManagement(InfluxDBProperties influxDBProperties) {
        this.influxDBProperties = influxDBProperties;
    }

    @PostConstruct
    private void init() {
        initializeInfluxDBClient();
        initializeWriteApi();
    }

    /**
     * <b>InfluxDBClient를 초기화합니다.</b>
     * <hr>
     * Double-Checked Locking 패턴을 사용하여 <br>
     * 다중 스레드 환경에서도 안전하게 객체를 초기화합니다.
     */
    private void initializeInfluxDBClient() {
        if (Objects.isNull(influxDBClient) || isInfluxDBClientClosed()) {
            synchronized (this) {
                if (Objects.isNull(influxDBClient) || isInfluxDBClientClosed()) {
                    influxDBClientClose();
                    influxDBClient = InfluxDBClientFactory.create(customInfluxDBClientOptions());
                }
            }
        }

    }

    /**
     * <b>WriteApi를 초기화합니다.</b>
     * <hr>
     * Double-Checked Locking 패턴을 사용하여 <br>
     * 다중 스레드 환경에서도 안전하게 객체를 초기화합니다.
     */
    private void initializeWriteApi() {
        if (Objects.isNull(writeApi) || isWriteApiClosed()) {
            synchronized (this) {
                if (Objects.isNull(writeApi) || isWriteApiClosed()) {
                    writeApiClose();
                    writeApi = influxDBClient.makeWriteApi(customWriteOptions());
                }
            }
        }
    }

    /**
     * InfluxDBClient의 연결 상태를 확인하고, <br>
     * 연결이 정상적이지 않으면 객체를 재초기화하여 반환합니다.
     *
     * @return influxDBClient
     */
    public InfluxDBClient influxDBClient() {
        if (isInfluxDBClientClosed()) {
            initializeInfluxDBClient();
        }
        return influxDBClient;
    }

    /**
     * WriteApi의 연결 상태를 확인하고, <br>
     * 연결이 정상적이지 않으면 비정상적인 객체들을 재초기화하여 반환합니다.
     *
     * @return writeApi
     */
    public WriteApi writeApi() {
        if (isWriteApiClosed()) {
            if (isInfluxDBClientClosed()) {
                initializeInfluxDBClient();
            }
            initializeWriteApi();
        }
        return writeApi;
    }

    /**
     * InfluxDBClient의 연결 상태를 반환합니다.
     *
     * @return 정상적으로 연결되어 있으면 true를 반환하고, 그렇지 않으면 false를 반환합니다.
     */
    public boolean isInfluxDBClientConnected() {
        try {
            influxDBClient.ping();
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

    /**
     * InfluxDBClient의 연결이 끊어졌는지 확인합니다.
     *
     * @return 연결이 끊어졌다면 true를 반환하고, 그렇지 않으면 false를 반환합니다.
     */
    private boolean isInfluxDBClientClosed() {
        return !isInfluxDBClientConnected();
    }

    /**
     * WriteApi의 연결 상태를 반환합니다.
     *
     * @return 정상적으로 연결되어 있으면 true를 반환하고, 그렇지 않으면 false를 반환합니다.
     */
    public boolean isWriteApiConnected() {
        try {
            writeApi.flush();
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

    /**
     * WriteApi의 연결이 끊어졌는지 확인합니다.
     *
     * @return 연결이 끊어졌다면 true를 반환하고, 그렇지 않으면 false를 반환합니다.
     */
    private boolean isWriteApiClosed() {
        return !isWriteApiConnected();
    }

    /**
     * InfluxDBClient의 연결을 종료합니다.
     */
    private void influxDBClientClose() {
        if (Objects.nonNull(influxDBClient)) {
            influxDBClient.close();
        }
    }

    /**
     * WriteApi의 연결을 종료합니다.
     */
    public void writeApiClose() {
        if (Objects.nonNull(writeApi)) {
            writeApi.flush();
            writeApi.close();
        }
    }

    /**
     * InfluxDB와 관련된 모든 연결을 종료합니다. <br>
     * InfluxDBClient와 WriteApi 두 객체 모두 종료됩니다.
     */
    public void allClose() {
        writeApiClose();
        influxDBClientClose();
    }

    /**
     * InfluxDBClient의 설정을 반환합니다. <br>
     * InfluxDB의 URL, 인증 정보 등을 설정하여 연결 옵션을 제공합니다.
     */
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

    /**
     * OkHttpClient 설정을 반환합니다. <br>
     * 연결 재시도 및 커넥션 풀을 설정합니다.
     */
    private OkHttpClient.Builder customOkHttpClientBuilder() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectionPool(customConnectionPool());
    }

    /**
     * OkHttpClient의 커넥션 풀 설정을 반환합니다. <br>
     * 커넥션 풀의 최대 연결 수와 연결 유지 기간을 설정합니다.
     */
    private ConnectionPool customConnectionPool() {
        return new ConnectionPool(2, 365, TimeUnit.DAYS);
    }

    /**
     * WriteApi의 설정을 반환합니다. <br>
     * 데이터 배치 크기, Flush 간격, Thread Pool 등을 설정합니다.
     */
    private WriteOptions customWriteOptions() {
        return WriteOptions.builder()
                .batchSize(100)  // 100개 모아서 한번에 전송
                .flushInterval(1000) // 1초마다 flush
                .writeScheduler(Schedulers.from(Executors.newFixedThreadPool(2))) // 직접 스레드풀 설정
                .build();
    }
}
