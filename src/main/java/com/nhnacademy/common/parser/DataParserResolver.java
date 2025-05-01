package com.nhnacademy.common.parser;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class DataParserResolver {

    private final List<DataParser> dataParsers;

    public DataParserResolver(List<DataParser> dataParsers) {
        this.dataParsers = dataParsers;
    }

    /**
     * 페이로드 구조에 맞는 parser 구현체를 가져옵니다.
     * @param payload 페이로드
     * @return parser 구현체
     */
    public DataParser getDataParser(String payload) {
        String data = payload.trim();
        for (DataParser dataParser : dataParsers) {
            if (dataParser.matchDataType(data)) {
                return dataParser;
            }
        }
        throw new NoSuchElementException("not found dataParser: %s".formatted(payload));
    }
}
