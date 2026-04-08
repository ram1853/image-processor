package com.image.processor.service;

import com.image.processor.common.dto.ImageMetaData;
import com.image.processor.helper.ImageCompressor;
import com.image.processor.helper.ImageDownloader;
import com.image.processor.helper.ImageUploader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;

@ExtendWith(MockitoExtension.class)
class ImageCompressionServiceTest {

    @Mock
    private ImageDownloader imageDownloader;

    @Mock
    private ImageCompressor imageCompressor;

    @Mock
    private ImageUploader imageUploader;

    @InjectMocks
    private ImageCompressionService imageCompressionService;

    @Test
    void startJob_withInput_shouldCompressAndUpload() {
        //Arrange
        String input = "{\"version\":\"0\",\"id\":\"70200c0a-7b08-f842-18f4-2305a778ac13\",\"detail-type\":\"Object Created\",\"source\":\"aws.s3\",\"account\":\"545009866715\",\"time\":\"2026-04-08T10:15:47Z\",\"region\":\"ap-south-1\",\"resources\":[\"arn:aws:s3:::image-storage-1853\"],\"detail\":{\"version\":\"0\",\"bucket\":{\"name\":\"image-storage-1853\"},\"object\":{\"key\":\"ram1853/cat.jpg\",\"size\":9051,\"etag\":\"62e5e2d0f33281380175b74d93ecfe91\",\"sequencer\":\"0069D62AD3D91421DC\"},\"request-id\":\"K4VTFMHC2T5YRC48\",\"requester\":\"545009866715\",\"source-ip-address\":\"223.185.135.66\",\"reason\":\"PutObject\"}}";
        ImageMetaData imageMetaData = ImageMetaData.builder().key("ram1853/dog.jpg").inputStream(InputStream.nullInputStream()).build();
        Mockito.when(imageDownloader.download(Mockito.any())).thenReturn(imageMetaData);
        Mockito.when(imageCompressor.compress(Mockito.any(), Mockito.anyString(), Mockito.anyFloat())).thenReturn(Mockito.anyString());

        //Act
        imageCompressionService.startJob(input);

        //Assert
        Mockito.verify(imageCompressor, Mockito.times(2)).compress(Mockito.any(), Mockito.any(), Mockito.anyFloat());
        Mockito.verify(imageUploader, Mockito.times(2)).upload(Mockito.anyString(), Mockito.any());
    }

    @Test
    void startJob_withInvalidInput_shouldNotCompressAndUpload() {
        //Arrange
        String input = "{\"version\":\"0\",\"id\":\"70200c0a-7b08-f842-18f4-2305a778ac13\",\"detail-type\":\"Object Created\",\"source\":\"aws.s3\",\"account\":\"545009866715\",\"time\":\"2026-04-08T10:15:47Z\",\"region\":\"ap-south-1\",\"resources\":[\"arn:aws:s3:::image-storage-1853\"],\"detail\":{\"version\":\"0\",\"bucket\":{\"name\":\"image-storage-1853\"},\"object\":{\"key\":\"cat.jpg\",\"size\":9051,\"etag\":\"62e5e2d0f33281380175b74d93ecfe91\",\"sequencer\":\"0069D62AD3D91421DC\"},\"request-id\":\"K4VTFMHC2T5YRC48\",\"requester\":\"545009866715\",\"source-ip-address\":\"223.185.135.66\",\"reason\":\"PutObject\"}}";

        //Act
        imageCompressionService.startJob(input);

        //Assert
        Mockito.verify(imageCompressor, Mockito.never()).compress(Mockito.any(), Mockito.any(), Mockito.anyFloat());
        Mockito.verify(imageUploader, Mockito.never()).upload(Mockito.anyString(), Mockito.any());
    }
}