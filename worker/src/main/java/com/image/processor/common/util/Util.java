package com.image.processor.common.util;

import com.image.processor.common.dto.ImageMetaData;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Slf4j
public final class Util {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Util() {}

    public static ImageMetaData getImageMetaData(String message) {
        JsonNode root = OBJECT_MAPPER.readTree(message);
        JsonNode detail = root.get("detail");
        String bucket = detail.get("bucket").get("name").stringValue();
        String key = detail.get("object").get("key").stringValue();

        return ImageMetaData.builder().bucket(bucket).key(key).build();
    }
}
