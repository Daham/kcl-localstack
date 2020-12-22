package com.example.kcldemo.config;

import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.testcontainers.containers.localstack.LocalStackContainer;

import javax.annotation.PostConstruct;

@Configuration
public class KinesisProducerConfiguration {

    @Autowired
    private LocalStackContainer localStackContainer;

    @Autowired
    private AmazonKinesis amazonKinesis;

    private static final int KINESIS_STREAM_SHARD_COUNT = 1;

    private static final String KINESIS_STREAM_NAME = "TEST_KINESIS_STREAM";

    @Bean
    @ConditionalOnMissingBean
    @DependsOn("localStackContainer")
    AmazonKinesis amazonKinesis() {
        System.setProperty(SDKGlobalConfiguration.AWS_CBOR_DISABLE_SYSTEM_PROPERTY, "true");

        return AmazonKinesisClientBuilder.standard()
                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.KINESIS))
                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
                .build();
    }

    @PostConstruct
    private void createTestKinesisStream() {

        if (!amazonKinesis.listStreams().getStreamNames().contains(KINESIS_STREAM_NAME)) {
            amazonKinesis.createStream(KINESIS_STREAM_NAME, KINESIS_STREAM_SHARD_COUNT);
        }
    }
}
