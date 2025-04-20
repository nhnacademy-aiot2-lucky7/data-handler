package com.nhnacademy.data.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DataParser {

    String getFileType();

    List<Map<String, Object>> parsing(File file) throws IOException;

    default boolean matchFileType(String fileName) {
        return fileName.trim().toLowerCase().endsWith(getFileType().toLowerCase());
    }
}
