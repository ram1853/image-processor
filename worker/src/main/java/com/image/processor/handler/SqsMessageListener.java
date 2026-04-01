package com.image.processor.handler;

import com.image.processor.business.ImageDownloader;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SqsMessageListener {

    private final ImageDownloader imageDownloader;

    @Autowired
    public SqsMessageListener(ImageDownloader imageDownloader) {
        this.imageDownloader = imageDownloader;
    }

    @SqsListener("${queue-name}")
    public void receiveMessage(String message) {
        log.info("Received sqs message: {}", message);
        imageDownloader.download(message);
    }
}
