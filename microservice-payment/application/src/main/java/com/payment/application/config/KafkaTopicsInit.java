package com.payment.application.config;

import com.payment.entity.dto.TopicConfigDto;
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
        createTopics(topicsConfig.getCreated());
        createTopics(topicsConfig.getAdded());
        createTopics(topicsConfig.getRemoved());
        createTopics(topicsConfig.getPayment());
        createTopics(topicsConfig.getCancelled());
        createTopics(topicsConfig.getSubmitted());
        createTopics(topicsConfig.getCompleted());
        createTopics(topicsConfig.getRetryTopic());
        createTopics(topicsConfig.getDeadLetterQueue());
    }

    private void createTopics(TopicConfigDto configDto) {
        try {
            NewTopic topic = new NewTopic(configDto.getName(), configDto.getPartitions(), configDto.getReplication().shortValue());
            kafkaAdmin.createOrModifyTopics(topic);
            log.info("Topic {} created or modified ", configDto.getName());
        } catch (Exception ex) {
            log.error("Error while creating kafka topic configuration", ex);
        }
    }
}
