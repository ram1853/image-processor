package com.image.processor.business;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
@Slf4j
public class ImageDownloader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void download(String sqsMessage) {
        JsonNode root = objectMapper.readTree(sqsMessage);
        JsonNode record = root.get("Records").get(0);
        String bucket = record.get("s3").get("bucket").get("name").stringValue();
        String key = record.get("s3").get("object").get("key").stringValue();

        log.info("bucket: {}", bucket);
        log.info("key: {}", key);
    }
}
