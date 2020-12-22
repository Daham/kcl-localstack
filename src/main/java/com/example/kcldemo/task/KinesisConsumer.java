package com.example.kcldemo.task;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.example.kcldemo.IpProcessorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.localstack.LocalStackContainer;

import java.util.concurrent.CompletableFuture;

import static com.example.kcldemo.util.Constants.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class KinesisConsumer {
    @Autowired
    private LocalStackContainer localStackContainer;

    @Autowired
    private AmazonKinesis amazonKinesis;

    @EventListener(ApplicationReadyEvent.class)
    public void configAndConsume() {

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials("accessKey", "secretKey");

        KinesisClientLibConfiguration consumerConfig = new KinesisClientLibConfiguration(
                KINESIS_APPLICATION_NAME,
                KINESIS_STREAM_NAME,
                new AWSStaticCredentialsProvider(awsCredentials),
                WORKER_ID)
                .withRegionName(Regions.US_EAST_1.getName())
                .withKinesisEndpoint(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.KINESIS).getServiceEndpoint())
                .withDynamoDBEndpoint(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.DYNAMODB).getServiceEndpoint())
                .withCloudWatchEndpoint(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.CLOUDWATCH).getServiceEndpoint())
                .withIsCBORProtocolDisabled(true);

        final Worker worker = new Worker.Builder()
                .recordProcessorFactory(new IpProcessorFactory())
                .config(consumerConfig)
                .build();
        CompletableFuture.runAsync(worker);
    }
}
