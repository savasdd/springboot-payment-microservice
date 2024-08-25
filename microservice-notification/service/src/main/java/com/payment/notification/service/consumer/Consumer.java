package com.payment.notification.service.consumer;

import com.payment.notification.common.content.KafkaContent;
import com.payment.notification.common.event.NotificationEvent;
import com.payment.notification.common.utils.SerializerUtil;
import com.payment.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class Consumer {

    private final SerializerUtil serializerUtil;
    private final NotificationService service;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = {"${topics.notification.name}"}, id = "orders-consumer")
    public void process(Acknowledgment ack, ConsumerRecord<String, byte[]> consumerRecord) {
        try {
            KafkaContent content = serializerUtil.deserialize(consumerRecord.value(), KafkaContent.class);
            NotificationEvent event = serializerUtil.serializeToDDto(content.getData(), NotificationEvent.class);
            ack.acknowledge();

            log.info("CONSUMER NOTIFICATION: {}", getRecordInfo(consumerRecord));
            service.sendNotification(event);
        } catch (Exception ex) {
            log.error("ack exception while processing record: {}", getRecordInfo(consumerRecord), ex);
        }
    }

    private String getRecordInfo(ConsumerRecord<String, byte[]> record) {
        return "Topic: " + record.topic() + ", Key: " + record.key() + ", Partition: " + record.partition() + ", Offset: " + record.offset() + ", Timestamp: " + record.timestamp() + ", Value: " + Arrays.toString(record.value()) + ", Headers: " + record.headers() + " ";
    }
}