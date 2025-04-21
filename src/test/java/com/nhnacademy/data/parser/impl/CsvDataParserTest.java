package com.nhnacademy.data.parser.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
class CsvDataParserTest {

    private final CsvDataParser parser = new CsvDataParser();

    private File file;

    private AtomicInteger index;

    @BeforeEach
    void setUp() throws URISyntaxException {
        index = new AtomicInteger();

        URL url = getClass().getClassLoader().getResource("test_sensor_data.csv");
        Assertions.assertNotNull(
                url,
                "resources 내에 해당하는 파일이 존재하지 않습니다: '%s'"
                        .formatted("test_sensor_data.csv")
        );

        file = new File(url.toURI()); // URL -> URI -> File
        Assertions.assertTrue(
                file.exists(),
                "해당 경로에 테스트 파일이 존재하지 않습니다: '%s'".formatted(url.getPath())
        );
    }

    /**
     * 실제로 들어오는 센서 데이터 구조에 따라서 <br>
     * parsing 방식이 달라질 수도 있습니다.
     */
    @DisplayName("데이터 구조 parsing 테스트")
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
