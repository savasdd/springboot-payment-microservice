package com.payment.common.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public DefaultKafkaProducerFactory<String, byte[]> getProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getProducerProps());
    }

    @Bean
    public KafkaTemplate<String, byte[]> getKafkaTemplate() {
        KafkaTemplate<String, byte[]> kafkaTemplate = new KafkaTemplate<>(getProducerFactory());
        kafkaTemplate.setMicrometerEnabled(true);
        return kafkaTemplate;
    }

    private Map<String, Object> getProducerProps() {
        return Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class,
                ProducerConfig.ACKS_CONFIG, "all",
                ProducerConfig.RETRIES_CONFIG, 5,
                ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000,
                ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 1068576,
                ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000
        );
    }
}
