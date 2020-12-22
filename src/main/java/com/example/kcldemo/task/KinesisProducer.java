package com.example.kcldemo.task;

import com.amazonaws.services.kinesis.AmazonKinesis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

@Component
@Slf4j
@RequiredArgsConstructor
public class KinesisProducer {

    @Autowired
    private AmazonKinesis amazonKinesis;

    private static final int MESSAGE_LIMIT = 100;

    private static final int INTER_MESSAGE_TIME_GAP = 5000;

    private static final String KINESIS_STREAM_NAME = "TEST_KINESIS_STREAM";

    @EventListener(ApplicationReadyEvent.class)
    public void startPublish() throws InterruptedException {
        int counter = 0;

        while (counter < MESSAGE_LIMIT) {
            publish("Test Data", "key-" + counter);
            Thread.sleep(INTER_MESSAGE_TIME_GAP);
            counter++;
        }
    }

    public void publish(String data, String partitionKey) {
        try {
            ByteBuffer buf = ByteBuffer.allocate(50);
            CharBuffer cbuf = buf.asCharBuffer();
            cbuf.put("Test Data");

            amazonKinesis.putRecord(KINESIS_STREAM_NAME, buf, partitionKey);
            log.info("Published data: {} with partition key: {}", data, partitionKey);
        } catch (Exception e) {
            log.error("Error occured while publishing data: {} with partition key: {}", data, partitionKey);
        }
    }
}
