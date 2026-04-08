package com.image.processor.helper;

import com.image.processor.common.dto.ImageMetaData;
import com.image.processor.common.exception.DownloadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.http.AbortableInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ImageDownloaderTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private ImageDownloader imageDownloader;

    @Test
    void download_withImageMetaData_shouldDownloadImage() {
        //Arrange
        ImageMetaData imageMetaData = ImageMetaData.builder()
                .bucket("test-bucket")
                .key("ram1853/dog.jpg")
                .build();

        byte[] bytes = "test-response".getBytes(StandardCharsets.UTF_8);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        GetObjectResponse response = GetObjectResponse.builder()
                .contentLength((long) bytes.length)
                .build();

        ResponseInputStream<GetObjectResponse> responseInputStream = new ResponseInputStream<>(response, AbortableInputStream.create(byteArrayInputStream));

        Mockito.when(s3Client.getObject(Mockito.any(GetObjectRequest.class))).thenReturn(responseInputStream);

        //Act
        ImageMetaData result = imageDownloader.download(imageMetaData);

        //Assert
        assertThat(result.inputStream(), notNullValue());
    }

    @Test
    void download_withInvalidImageMetaData_shouldThrowException() {
        //Arrange
        ImageMetaData imageMetaData = ImageMetaData.builder()
                .bucket("non-existing-bucket")
                .key("ram1853/dog.jpg")
                .build();

        Mockito.when(s3Client.getObject(Mockito.any(GetObjectRequest.class))).thenThrow(new RuntimeException());

        //Act and assert
        assertThrows(DownloadException.class, () -> imageDownloader.download(imageMetaData));
    }

}