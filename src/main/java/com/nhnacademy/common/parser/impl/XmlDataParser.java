package com.nhnacademy.common.parser.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.nhnacademy.common.parser.DataParser;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public final class XmlDataParser implements DataParser {

    private static final String FILE_TYPE = "XML";

    private final XmlMapper xmlMapper;

    public XmlDataParser() {
        this.xmlMapper = new XmlMapper();
    }

    @Override
    public String getFileType() {
        return FILE_TYPE;
    }

    @Override
    public boolean matchDataType(String payload) {
        return payload.startsWith("<")
                && payload.endsWith(">")
                && payload.contains("</");
    }

    @Override
    public Map<String, Object> parsing(String payload) throws IOException {
        return xmlMapper.readValue(
                payload,
                new TypeReference<>() {
                }
        );
    }

    @Override
    public List<Map<String, Object>> parsing(File file) throws IOException {
        return xmlMapper.readValue(
                file,
                new TypeReference<>() {
                }
        );
    }
}
