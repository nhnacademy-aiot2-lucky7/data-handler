package com.nhnacademy.parser;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataParserResolver {

    private final List<DataParser> dataParsers;

    public DataParserResolver(List<DataParser> dataParsers) {
        this.dataParsers = dataParsers;
    }

    public DataParser getDataParser(String payload) {
        String data = payload.trim();
        for (DataParser dataParser : dataParsers) {
            if (dataParser.matchDataType(data)) {
                return dataParser;
            }
        }
        return null;
    }

    /*public DataParser getDataParser(String fileName) {
        for (DataParser dataParser : dataParsers) {
            if (dataParser.matchFileType(fileName)) {
                return dataParser;
            }
        }
        return null;
    }*/
}
