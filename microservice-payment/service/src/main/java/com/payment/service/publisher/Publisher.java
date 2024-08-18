package com.payment.service.publisher;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

public interface Publisher {
    void publish(String topic, Object data);

    void publish(String topic, String key, Object data);

    void publish(String topic, String key, Object data, Map<String, byte[]> headers);

    void publishRetry(String topic, byte[] data, Map<String, byte[]> headers);

    void publishRetry(String topic, String key, byte[] data, Map<String, byte[]> headers);

    void publishRetry(String topic, String key, ConsumerRecord<String, byte[]> record);
}
