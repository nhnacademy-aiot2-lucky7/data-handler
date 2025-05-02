package com.nhnacademy.parser;

import com.nhnacademy.common.parser.DataParser;
import com.nhnacademy.common.parser.DataParserResolver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 교재 내용 검증
 *
 * @see <a href="https://github.com/nhnacademy-bootcamp/spring-blog/blob/main/docs/06.spring-boot/07.%5B%EC%8B%A4%EC%8A%B5%5D-spring-boot-di.adoc">출처</a>
 */
@Slf4j
@SpringBootTest
class DataParserResolverTest {

    @Autowired
    private DataParserResolver dataParserResolver;

    /**
     * SpringBootTest 환경으로 생성자 주입된 구현체
     */
    @Autowired
    private Set<DataParser> actual;

    /**
     * DataParserResolver 내에 생성자 주입된 구현체
     */
    private Set<DataParser> expected;

    @BeforeEach
    void setUp() throws Exception {
        // 리플렉션 API으로 private final 필드 접근
        Field field = DataParserResolver.class.getDeclaredField("dataParsers");
        field.setAccessible(true); // private 접근 허용

        // 실제 필드 값 가져오기
        Object value = field.get(dataParserResolver);

        // 필드 값의 타입 확인
        assertThat(value).isInstanceOf(List.class);
        List<?> rawList = (List<?>) value;

        // 데이터 변환 및 타입 검증
        expected = rawList.stream()
                .peek(element -> assertThat(element)
                        .describedAs("잘못된 DataParser 인터페이스 구현체입니다: %s", element.getClass())
                        .isInstanceOf(DataParser.class))
                .map(DataParser.class::cast)
                .collect(Collectors.toSet());

        assertThat(expected).isNotEmpty();
    }

    /**
     * DataParserResolver 필드 내에 존재하는 List<DataParser> dataParsers 검증
     */
    @DisplayName("Spring Boot - DataParser 구현체들의 Bean 리스트 주입 테스트")
    @Test
    void testDataParsersFieldInjected() {
        assertEquals(expected.size(), actual.size());
        assertThat(actual)
                .describedAs("실제 구현체의 구조가 다릅니다.")
                .isEqualTo(expected);
    }
}
