package com.nhnacademy.common.parser.impl;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.nhnacademy.common.parser.DataParser;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public final class CsvDataParser implements DataParser {

    private static final String FILE_TYPE = "CSV";

    private final CsvMapper csvMapper;

    private final CsvSchema csvSchema;

    public CsvDataParser() {
        this.csvMapper = new CsvMapper();
        this.csvSchema = CsvSchema.emptySchema().withHeader();
    }

    @Override
    public String getFileType() {
        return FILE_TYPE;
    }

    @Override
    public boolean matchDataType(String payload) {
        return payload.contains("\n") && payload.contains(",")
                && !payload.contains("{") && !payload.contains("<");
    }

    @Override
    public Map<String, Object> parsing(String payload) throws IOException {
        try (MappingIterator<Map<String, Object>> it =
                     csvMapper.readerFor(Map.class)
                             .with(csvSchema)
                             .readValues(payload)
        ) {
            return it.hasNext() ? it.next() : Map.of();
        }
    }

    public List<Map<String, Object>> parsingAll(String payload) throws IOException {
        try (MappingIterator<Map<String, Object>> it =
                     csvMapper.readerFor(Map.class)
                             .with(csvSchema)
                             .readValues(payload)
        ) {
            return it.hasNext() ? it.readAll() : List.of();
        }
    }

    @Override
    public List<Map<String, Object>> parsing(File file) throws IOException {
        try (MappingIterator<Map<String, Object>> it =
                     csvMapper.readerFor(Map.class)
                             .with(csvSchema)
                             .readValues(file)
        ) {
            return it.hasNext() ? it.readAll() : List.of();
        }
    }
}
