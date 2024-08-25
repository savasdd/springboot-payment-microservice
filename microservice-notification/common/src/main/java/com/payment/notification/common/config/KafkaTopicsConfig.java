package com.payment.notification.common.config;

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

    private KafkaTopicsConfigDto notification;

    @PostConstruct
    public void configProperties() {
        log.info("Creating topic config {}", getTopicName());
    }

    private String getTopicName() {
        return Stream.of(notification).map(KafkaTopicsConfigDto::getName).collect(Collectors.joining(", ", "[", "]"));
    }

    public String getTopicName(String eventType) throws Exception {
        return switch (eventType) {
            case "NOTIFICATION" -> notification.getName();
            default -> throw new Exception("Type not found");
        };
    }
}