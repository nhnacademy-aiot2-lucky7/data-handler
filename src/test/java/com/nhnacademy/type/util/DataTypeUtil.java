package com.nhnacademy.type.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@Deprecated
public final class DataTypeUtil {

    private final Map<String, String> dataTypes = new ConcurrentHashMap<>();

    private final File file;

    private final ObjectMapper objectMapper;

    private final ObjectWriter objectWriter;

    public DataTypeUtil(ObjectMapper objectMapper) {
        this.file = new File(
                System.getProperty("user.dir"),
                "data/sensor_data.json"
        );
        this.objectMapper = objectMapper;
        this.objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
    }

    @PostConstruct
    private void init() {
        try {
            if (!file.exists()) {
                File parentDir = file.getParentFile();
                if (Objects.nonNull(parentDir) && !parentDir.exists()) {
                    parentDir.mkdirs();
                }
                objectWriter.writeValue(file, dataTypes);
            }
            dataTypes.putAll(objectMapper.readValue(file, new TypeReference<>() {
            }));
        } catch (IOException ignored) {
            dataTypes.putAll(Map.of());
        }
    }

    private synchronized void registerDataType(String key) {
        if (contains(key)) {
            return;
        }
        dataTypes.put(key, key);
        try {
            objectWriter.writeValue(file, dataTypes);
        } catch (IOException e) {
            log.error("{}", e.getMessage(), e);
        }
    }

    private boolean contains(String key) {
        return dataTypes.containsKey(key);
    }

    /// TODO: 추후 이곳을 데이터 타입을 얻는 목적은 동일 하지만,
    /// 방식이 조금 바뀌어, 타입의 일관성을 지키는 동시에 새로운 유형의 데이터 타입은 센서 서비스에게 저장을 요청합니다.
    public String get(String key) {
        if (!contains(key)) {
            registerDataType(key);
        }
        return dataTypes.get(key);
    }
}
