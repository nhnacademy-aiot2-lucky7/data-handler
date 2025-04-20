package com.nhnacademy.data.parser.impl;

import com.nhnacademy.data.parser.DataParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CsvDataParser implements DataParser {

    private static final String FILE_TYPE = "CSV";

    @Override
    public String getFileType() {
        return FILE_TYPE;
    }

    @Override
    public List<Map<String, Object>> parsing(File file) throws IOException {
        // 최신 Builder API 사용
        CSVFormat format =
                CSVFormat.DEFAULT.builder()
                        .setHeader()                // 첫 줄을 헤더로 인식
                        .setSkipHeaderRecord(true)  // 첫 줄을 레코드에서 제외
                        .get();
        try (
                CSVParser parser =
                        CSVParser.builder()
                                .setFile(file)
                                .setFormat(format)
                                .get()
        ) {
            List<Map<String, Object>> result = new ArrayList<>();

            for (CSVRecord record : parser) {
                /*Map<String, Object> row = new HashMap<>();
                for (Map.Entry<String, String> entry : record.toMap().entrySet()) {
                    row.put(entry.getKey(), entry.getValue()); // String → Object (업캐스팅)
                }
                result.add(row);*/
                result.add(new HashMap<>(record.toMap()));
            }
            return result;
        }
    }
}
