package com.nhnacademy.parser;

import com.nhnacademy.common.parser.DataParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @deprecated {@code Apache Commons CSV} 라이브러리 대신 <br>
 * {@code Jackson Dataformat CSV} 라이브러리 기반으로 구현합니다. <br>
 * 하지만, 추후에 재사용할 수 있습니다.
 */
public class OldCsvDataParser implements DataParser {

    private static final String FILE_TYPE = "CSV";

    // private CsvMapper csvMapper;

    @Override
    public String getFileType() {
        return FILE_TYPE;
    }

    @Override
    public boolean matchDataType(String payload) {
        return false;
    }

    @Override
    public Map<String, Object> parsing(String payload) throws IOException {
        return Map.of();
    }

    /**
     * @see <a href="https://commons.apache.org/proper/commons-csv/apidocs/index.html">사용법 가이드</a>
     */
    @Override
    public List<Map<String, Object>> parsing(File file) throws IOException {
        // 최신 Builder API 사용
        CSVFormat format =
                CSVFormat.DEFAULT.builder()
                        .setHeader()                // 첫 줄을 헤더로 인식
                        .setSkipHeaderRecord(true)  // 첫 줄을 레코드에서 제외
                        .setDelimiter(',')          // CSV 칸 구분자 지정
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
