package com.image.processor.helper;

import com.image.processor.common.exception.CompressionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.image.BufferedImage;

class ImageCompressorTest {

    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    @Test
    void compress_withCompressionDetails_shouldCompressImage() {
        //Arrange
        ImageCompressor imageCompressor = new ImageCompressor();
        BufferedImage bufferedImage = new BufferedImage(5, 5, BufferedImage.TYPE_3BYTE_BGR);
        String outputFileName = "/Users/ram/Downloads/dog.jpg";
        float quality = 0.25f;

        //Act
        String absolutePath = imageCompressor.compress(bufferedImage, outputFileName, quality);

        //Assert
        assertThat(absolutePath, is("/Users/ram/Downloads/dog.jpg"));
    }

    @Test
    void compress_withInvalidCompressionDetails_shouldThrowException() {
        //Arrange
        ImageCompressor imageCompressor = new ImageCompressor();
        BufferedImage bufferedImage = new BufferedImage(5, 5, BufferedImage.TYPE_3BYTE_BGR);
        String outputFileName = "/invalidPath/Users/ram/Downloads/dog.jpg";
        float quality = 0.25f;

        //Act and assert
        assertThrows(CompressionException.class, () -> imageCompressor.compress(bufferedImage, outputFileName, quality));
    }
}