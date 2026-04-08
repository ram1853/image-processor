package com.image.processor.service;

import com.image.processor.common.dto.ImageMetaData;
import com.image.processor.helper.ImageCompressor;
import com.image.processor.helper.ImageDownloader;
import com.image.processor.common.util.Util;
import com.image.processor.helper.ImageUploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@Service
@Slf4j
public class ImageCompressionService {

    private final ImageDownloader imageDownloader;
    private final ImageCompressor imageCompressor;
    private final ImageUploader imageUploader;

    @Autowired
    public ImageCompressionService(ImageDownloader imageDownloader, ImageCompressor imageCompressor,
                                   ImageUploader imageUploader) {
        this.imageDownloader = imageDownloader;
        this.imageCompressor = imageCompressor;
        this.imageUploader   = imageUploader;
    }

    public void startJob(String input) {
        log.info("In ImageCompressionService, received input: {}", input);

        try {
            ImageMetaData imageMetaData =
                    imageDownloader.download(Util.getImageMetaData(input));

            BufferedImage inputImage = ImageIO.read(imageMetaData.inputStream());

            String key = imageMetaData.key();
            String userName = key.substring(0, key.indexOf('/'));
            String originalFileName = key.substring(key.lastIndexOf('/') + 1, key.lastIndexOf('.'));

            compressAndUpload(inputImage, imageMetaData, userName,
                    originalFileName, "_compressed_medium.jpg", 0.5f);

            compressAndUpload(inputImage, imageMetaData, userName,
                    originalFileName, "_compressed_small.jpg", 0.25f);

            log.info("Job success");

        } catch (Exception e) {
            log.error("Job failed", e);
        }
    }

    private void compressAndUpload(BufferedImage inputImage, ImageMetaData imageMetaData,
                                   String userName, String originalFileName,
                                   String suffix, float quality) {

        String outputFileName = originalFileName + suffix;
        String outputFilePath = imageCompressor.compress(inputImage, outputFileName, quality);
        String compressedKey = userName + '/' + outputFileName;
        imageUploader.upload(outputFilePath, imageMetaData.toBuilder().key(compressedKey).build());
    }
}
