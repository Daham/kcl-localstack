package com.example.kcldemo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class LocalStackAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LocalStackContainer localStackContainer() {
        DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:latest");
        LocalStackContainer localStackContainer = new LocalStackContainer(localstackImage)
                .withServices(LocalStackContainer.Service.KINESIS, LocalStackContainer.Service.DYNAMODB, LocalStackContainer.Service.CLOUDWATCH);
        localStackContainer.start();
        return localStackContainer;
    }
}