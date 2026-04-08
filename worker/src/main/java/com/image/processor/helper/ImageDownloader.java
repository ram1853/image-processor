package com.image.processor.helper;

import com.image.processor.common.dto.ImageMetaData;
import com.image.processor.common.exception.DownloadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@Component
@Slf4j
public class ImageDownloader {

    private final S3Client s3Client;

    @Autowired
    public ImageDownloader(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public ImageMetaData download(ImageMetaData imageMetaData) {
        log.info("In ImageDownloader, received imageMetaData: {}", imageMetaData);
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(imageMetaData.bucket())
                    .key(imageMetaData.key())
                    .build();

            return imageMetaData.toBuilder()
                    .inputStream(s3Client.getObject(getObjectRequest))
                    .build();
        } catch (Exception e) {
            log.error("while image download", e);
            throw new DownloadException("Image Download failed");
        }
    }
}
