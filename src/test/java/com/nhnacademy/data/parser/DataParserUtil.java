package com.nhnacademy.data.parser;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class DataParserUtil {

    public static String getPayload(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void print(List<Map<String, Object>> result) {
        AtomicInteger index = new AtomicInteger();
        log.debug("==================================================================================================");
        result.forEach(map -> {
            log.debug("[index: {}]", index.incrementAndGet());
            map.forEach((k, v) ->
                    log.debug("\t{\"{}\": {}}", k, v)
            );
        });
        log.debug("==================================================================================================");
    }
}
