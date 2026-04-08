package com.image.processor.helper;

import com.image.processor.common.dto.ImageMetaData;
import com.image.processor.common.exception.UploadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ImageUploaderTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private ImageUploader imageUploader;

    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    @Test
    void upload_ofCompressedFile_shouldUpload() {
        //Arrange
        ImageMetaData imageMetaData = ImageMetaData.builder().key("ram1853/dog_small.jpg").build();
        String filePath = "/Users/ram/Downloads/dog.jpg";
        Mockito.when(s3Client.putObject(Mockito.any(PutObjectRequest.class), Mockito.any(RequestBody.class)))
                .thenReturn((PutObjectResponse) PutObjectResponse.builder().sdkHttpResponse(SdkHttpResponse.builder()
                        .statusCode(200).build()).build());

        //Act
        imageUploader.upload(filePath, imageMetaData);

        //Assert
        Mockito.verify(s3Client, Mockito.times(1))
                .putObject(Mockito.any(PutObjectRequest.class), Mockito.any(RequestBody.class));
    }

    @Test
    void upload_withInvalidData_shouldThrowException() {
        //Arrange
        ImageMetaData imageMetaData = ImageMetaData.builder().key("ram1853/dog_small.jpg").build();
        String filePath = "/invalidPath/Users/ram/Downloads/dog.jpg";

        //Act and assert
        assertThrows(UploadException.class, () -> imageUploader.upload(filePath, imageMetaData));
    }
}