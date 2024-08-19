package com.payment.application.config;

import com.payment.entity.dto.TopicConfigDto;
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

    private TopicConfigDto retryTopic;
    private TopicConfigDto deadLetterQueue;
    private TopicConfigDto created;
    private TopicConfigDto added;
    private TopicConfigDto removed;
    private TopicConfigDto payment;
    private TopicConfigDto cancelled;
    private TopicConfigDto submitted;
    private TopicConfigDto completed;

    @PostConstruct
    public void configProperties() {
        log.info("Creating topic config {}", getTopicName());
    }

    private String getTopicName() {
        return Stream.of(created, added, removed, payment, cancelled, submitted, completed, retryTopic, deadLetterQueue).map(TopicConfigDto::getName).collect(Collectors.joining(", ", "[", "]"));
    }
}
