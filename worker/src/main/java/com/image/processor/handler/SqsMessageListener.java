package com.image.processor.handler;

import com.image.processor.service.ImageCompressionService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SqsMessageListener {

    private final ImageCompressionService imageCompressionService;

    @Autowired
    public SqsMessageListener(ImageCompressionService imageCompressionService) {
        this.imageCompressionService = imageCompressionService;
    }

    @SqsListener("${queue-name}")
    public void receiveMessage(String message) {
        log.info("In SqsMessageListener, Received sqs message: {}", message);
        imageCompressionService.startJob(message);
    }
}
