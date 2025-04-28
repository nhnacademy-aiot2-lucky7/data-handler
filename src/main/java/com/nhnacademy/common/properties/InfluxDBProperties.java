package com.nhnacademy.common.properties;

import com.nhnacademy.common.TransferMode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "influxdb")
public class InfluxDBProperties {

    private TransferMode writeMode = TransferMode.SYNC;

    private String url;

    private String token;

    private String org;

    private String bucket;

    public boolean isSyncMode() {
        return writeMode.equals(TransferMode.SYNC);
    }

    public boolean isAsyncMode() {
        return writeMode.equals(TransferMode.ASYNC);
    }

    public char[] getToken() {
        return token.toCharArray();
    }
}
