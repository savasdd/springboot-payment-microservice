package com.payment.user.service.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PublisherKafka implements Publisher {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(String topic, Object data) {
        try {
            ProducerRecord<String, byte[]> message = new ProducerRecord<>(topic, objectMapper.writeValueAsBytes(data));
            var result = kafkaTemplate.send(message);

            log.info("publish kafka: {}", result);
        } catch (Exception e) {
            log.error("publish kafka error", e);
        }
    }

    @Override
    public void publish(String topic, String key, Object data) {
        try {
            ProducerRecord<String, byte[]> message = new ProducerRecord<>(topic, key, objectMapper.writeValueAsBytes(data));
            var result = kafkaTemplate.send(message);

            log.info("PUBLISH KAFKA: {} {}", data, topic);
        } catch (Exception e) {
            log.error("Publish kafka error", e);
        }
    }

    @Override
    public void publish(String topic, String key, Object data, Map<String, byte[]> headers) {
        try {
            ProducerRecord<String, byte[]> message = new ProducerRecord<>(topic, objectMapper.writeValueAsBytes(data));
            headers.forEach((k, v) -> message.headers().add(k, v));
            var result = kafkaTemplate.send(message);

            log.info("publish kafka with headers: {}", result);
        } catch (Exception e) {
            log.error("publish kafka error", e);
        }
    }

    @Override
    public void publishRetry(String topic, byte[] data, Map<String, byte[]> headers) {
        try {
            ProducerRecord<String, byte[]> message = new ProducerRecord<>(topic, data);
            headers.forEach((k, v) -> message.headers().add(k, v));
            var result = kafkaTemplate.send(message);

            log.info("publish kafka retry with headers: {}", result);
        } catch (Exception e) {
            log.error("publish kafka error", e);
        }
    }

    @Override
    public void publishRetry(String topic, String key, byte[] data, Map<String, byte[]> headers) {
        try {
            ProducerRecord<String, byte[]> message = new ProducerRecord<>(topic, key, data);
            headers.forEach((k, v) -> message.headers().add(k, v));
            var result = kafkaTemplate.send(message);

            log.info("publish kafka retry with key: {}", result);
        } catch (Exception e) {
            log.error("publish kafka error", e);
        }
    }

    @Override
    public void publishRetry(String topic, String key, ConsumerRecord<String, byte[]> record) {
        try {
            ProducerRecord<String, byte[]> message = new ProducerRecord<>(topic, record.value());
            var result = kafkaTemplate.send(message);

            log.info("publish kafka retry with record: {}", result);
        } catch (Exception e) {
            log.error("publish kafka error", e);
        }
    }
}