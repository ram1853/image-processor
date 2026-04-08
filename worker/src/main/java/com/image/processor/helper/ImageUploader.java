package com.image.processor.helper;

import com.image.processor.common.dto.ImageMetaData;
import com.image.processor.common.exception.UploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.nio.file.Paths;

@Component
@Slf4j
public class ImageUploader {

    private final S3Client s3Client;

    @Autowired
    public ImageUploader(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void upload(String filePath, ImageMetaData imageMetaData) {
        log.info("In ImageUploader, received filePath: {}, imageMetaData: {}", filePath, imageMetaData);
        try {
            RequestBody requestBody = RequestBody.fromFile(Paths.get(filePath).toFile());
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(imageMetaData.bucket())
                    .key(imageMetaData.key())
                    .build();
            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, requestBody);
            log.info("Upload status of {}: {}", imageMetaData.key(), putObjectResponse.sdkHttpResponse().statusCode());
        } catch (Exception e) {
            log.error("while uploading", e);
            throw new UploadException("Failed to upload image");
        }
    }
}
