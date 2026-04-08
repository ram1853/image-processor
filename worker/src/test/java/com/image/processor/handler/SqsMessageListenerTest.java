package com.image.processor.handler;

import com.image.processor.service.ImageCompressionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SqsMessageListenerTest {

    @Mock
    private ImageCompressionService imageCompressionService;

    @InjectMocks
    private SqsMessageListener sqsMessageListener;

    @Test
    void receiveMessage_withMessage_shouldStartJob() {
        //Arrange
        String message = "input sqs message";

        //Act
        sqsMessageListener.receiveMessage(message);

        //Assert
        Mockito.verify(imageCompressionService, Mockito.times(1)).startJob(message);
    }

}