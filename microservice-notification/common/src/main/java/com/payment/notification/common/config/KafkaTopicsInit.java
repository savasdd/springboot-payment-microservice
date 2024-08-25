package com.payment.notification.common.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaTopicsInit {
    private final KafkaAdmin kafkaAdmin;
    private final KafkaTopicsConfig topicsConfig;

    @PostConstruct
    public void init() {
        createTopics(topicsConfig.getNotification());
    }

    private void createTopics(KafkaTopicsConfigDto configDto) {
        try {
            NewTopic topic = new NewTopic(configDto.getName(), configDto.getPartitions(), configDto.getReplication().shortValue());
            kafkaAdmin.createOrModifyTopics(topic);
            log.info("Topic {} created or modified ", configDto.getName());
        } catch (Exception ex) {
            log.error("Error while creating kafka topic configuration", ex);
        }
    }
}