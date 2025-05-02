package com.nhnacademy.parser.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.common.parser.impl.JsonDataParser;
import com.nhnacademy.parser.DataParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Slf4j
// @ExtendWith: JUnit 5 테스트 클래스에 해당되는 class 기능을 확장합니다.
@ExtendWith(SpringExtension.class) // SpringExtension.class: Spring의 테스트 환경
@ContextConfiguration(classes = {ObjectMapper.class, JsonDataParser.class}) // 최소한의 Bean 등록만 하면서, 해당하는 class 객체를 불러오기
class JsonDataParserTest {

    @Autowired
    private JsonDataParser parser;

    private File file;

    @BeforeEach
    void setUp() throws URISyntaxException {
        URL url = getClass().getClassLoader().getResource("test_sensor_data.json");
        Assertions.assertNotNull(
                url,
                "resources 내에 해당하는 파일이 존재하지 않습니다: '%s'"
                        .formatted("test_sensor_data.json")
        );

        file = new File(url.toURI()); // URL -> URI -> File
        Assertions.assertTrue(
                file.exists(),
                "해당 경로에 테스트 파일이 존재하지 않습니다: '%s'".formatted(url.getPath())
        );
    }

    /**
     * 실제로 들어오는 센서 데이터에 따라서 <br>
     * parsing 방식이 달라질 수도 있습니다.
     */
    @DisplayName("Data File - parsing 테스트")
    @Test
    void testFileParsing() throws IOException {
        List<Map<String, Object>> result = parser.parsing(file);
        DataParserUtil.print(result);
    }

    @DisplayName("Payload - parsing 테스트")
    @Test
    void testStringParsing() throws IOException {
        String payload = DataParserUtil.getPayload(file);
        log.debug("[JSON Payload]");
        log.debug("\n{}", payload);

        Map<String, Object> result = parser.parsing(payload);
        DataParserUtil.print(result);
    }
}
