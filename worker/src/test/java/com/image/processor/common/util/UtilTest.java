package com.image.processor.common.util;

import com.image.processor.common.dto.ImageMetaData;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class UtilTest {

    @Test
    void getImageMetaData_withMessage_returnsImageMetadata() {
        //Arrange
        String message = "{\"version\":\"0\",\"id\":\"70200c0a-7b08-f842-18f4-2305a778ac13\",\"detail-type\":\"Object Created\",\"source\":\"aws.s3\",\"account\":\"545009866715\",\"time\":\"2026-04-08T10:15:47Z\",\"region\":\"ap-south-1\",\"resources\":[\"arn:aws:s3:::image-storage-1853\"],\"detail\":{\"version\":\"0\",\"bucket\":{\"name\":\"image-storage-1853\"},\"object\":{\"key\":\"ram1853/cat.jpg\",\"size\":9051,\"etag\":\"62e5e2d0f33281380175b74d93ecfe91\",\"sequencer\":\"0069D62AD3D91421DC\"},\"request-id\":\"K4VTFMHC2T5YRC48\",\"requester\":\"545009866715\",\"source-ip-address\":\"223.185.135.66\",\"reason\":\"PutObject\"}}";

        //Act
        ImageMetaData imageMetaData = Util.getImageMetaData(message);

        //Assert
        assertThat(imageMetaData.bucket(), is("image-storage-1853"));
        assertThat(imageMetaData.key(), is("ram1853/cat.jpg"));
    }
}