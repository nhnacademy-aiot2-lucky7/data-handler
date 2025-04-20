package com.nhnacademy.data.parser.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@SpringBootTest
class JsonDataParserTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private JsonDataParser parser;

    private File file;

    private AtomicInteger index;

    @BeforeEach
    void setUp() throws IOException {
        index = new AtomicInteger();
        Resource resource =
                resourceLoader
                        .getResource("classpath:test_sensor_data.json");
        file = resource.getFile();
    }

    @DisplayName("문서 추가 예정...")
    @Test
    void testParsing() throws IOException {
        log.debug("==================================================================================================");
        List<Map<String, Object>> result = parser.parsing(file);
        result.forEach(map -> {
            log.debug("[index: {}]", index.incrementAndGet());
            map.forEach((k, v) ->
                    log.debug("\t{\"{}\": {}}", k, v)
            );
        });
        log.debug("==================================================================================================");
    }
}
