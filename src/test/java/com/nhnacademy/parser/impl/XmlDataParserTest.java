package com.nhnacademy.parser.impl;

import com.nhnacademy.common.parser.impl.XmlDataParser;
import com.nhnacademy.parser.DataParserUtil;
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

@Slf4j
class XmlDataParserTest {

    private final XmlDataParser parser = new XmlDataParser();

    private File file;

    @BeforeEach
    void setUp() throws URISyntaxException {
        URL url = getClass().getClassLoader().getResource("test_sensor_data.xml");
        Assertions.assertNotNull(
                url,
                "resources 내에 해당하는 파일이 존재하지 않습니다: '%s'"
                        .formatted("test_sensor_data.xml")
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
        log.debug("[XML Payload]");
        log.debug("\n{}", payload);

        Map<String, Object> result = parser.parsing(payload);
        DataParserUtil.print(result);
    }
}
