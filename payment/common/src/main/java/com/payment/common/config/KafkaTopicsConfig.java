package com.payment.common.config;

import com.payment.common.enums.EventType;
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
    private KafkaTopicsConfigDto notification;

    @PostConstruct
    public void configProperties() {
        log.info("Creating topic config {}", getTopicName());
    }

    private String getTopicName() {
        return Stream.of(created, added, removed, payment, cancelled, submitted, completed, retryTopic, deadLetterQueue, notification).map(KafkaTopicsConfigDto::getName).collect(Collectors.joining(", ", "[", "]"));
    }

    public String getTopicName(EventType eventType) {
        return switch (eventType) {
            case CREATED -> created.getName();
            case CANCELLED -> cancelled.getName();
            case COMPLETED -> completed.getName();
            case PAID -> payment.getName();
            case ADDED -> added.getName();
            case REMOVED -> removed.getName();
            case SUBMITTED -> submitted.getName();
            case NOTIFICATION -> notification.getName();
        };
    }
}
