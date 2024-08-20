package com.payment.common.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "topics")
public class KafkaTopicsConfig {

    private KafkaTopicsConfigDto retryTopic;
    private KafkaTopicsConfigDto deadLetterQueue;
    private KafkaTopicsConfigDto created;
    private KafkaTopicsConfigDto added;
    private KafkaTopicsConfigDto removed;
    private KafkaTopicsConfigDto payment;
    private KafkaTopicsConfigDto cancelled;
    private KafkaTopicsConfigDto submitted;
    private KafkaTopicsConfigDto completed;

    @PostConstruct
    public void configProperties() {
        log.info("Creating topic config {}", getTopicName());
    }

    private String getTopicName() {
        return Stream.of(created, added, removed, payment, cancelled, submitted, completed, retryTopic, deadLetterQueue).map(KafkaTopicsConfigDto::getName).collect(Collectors.joining(", ", "[", "]"));
    }
}
