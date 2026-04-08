package com.image.processor.helper;

import com.image.processor.common.exception.CompressionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

@Component
@Slf4j
public class ImageCompressor {

    public String compress(BufferedImage inputImage, String outputFileName, float quality) {
        log.info("In ImageCompressor, received inputImage: {}, outputFileName: {}, quality: {}",
                inputImage, outputFileName, quality);

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("No JPG writers available");
        }

        ImageWriter writer = writers.next();
        File outputFile = new File(outputFileName);

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputFile)) {

            writer.setOutput(ios);

            ImageWriteParam params = writer.getDefaultWriteParam();
            params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            params.setCompressionQuality(quality);

            writer.write(null, new IIOImage(inputImage, null, null), params);

            log.info("compression to {}% success", quality * 100);

            return outputFile.getAbsolutePath();
        } catch (Exception e) {
            log.error("while image compression", e);
            throw new CompressionException("Image compression failed");
        } finally {
            writer.dispose();
        }
    }
}
