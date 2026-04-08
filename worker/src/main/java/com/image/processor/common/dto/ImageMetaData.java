package com.image.processor.common.dto;

import lombok.Builder;

import java.io.InputStream;

@Builder(toBuilder = true)
public record ImageMetaData (String bucket, String key, InputStream inputStream) {
}
