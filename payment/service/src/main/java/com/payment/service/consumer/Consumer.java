package com.payment.service.consumer;

import com.payment.common.config.KafkaTopicsConfig;
import com.payment.common.utils.SerializerUtil;
import com.payment.entity.model.OutboxOrder;
import com.payment.service.publisher.Publisher;
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
    private static final String RETRY_COUNT_HEADER = "retryCount";
    private static final Integer MAX_RETRY_COUNT = 5;
    private static final Long PUBLISH_RETRY_COUNT = 5L;
    private static final Long PUBLISH_RETRY_BACKOFF_DURATION_MILLIS = 3000L;

    private final KafkaTopicsConfig topicsConfig;
    private final SerializerUtil serializerUtil;
    private final Publisher publisher;


    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = {"${topics.created.name}", "${topics.added.name}", "${topics.removed.name}", "${topics.payment.name}", "${topics.cancelled.name}", "${topics.submitted.name}", "${topics.completed.name}"}, id = "orders-consumer")
    public void process(Acknowledgment ack, ConsumerRecord<String, byte[]> consumerRecord) {
        try {
            OutboxOrder outboxOrder = serializerUtil.deserialize(consumerRecord.value(), OutboxOrder.class);
            ack.acknowledge();

            log.info("CONSUMER RECORD: {}", getRecordInfo(consumerRecord));
        } catch (Exception ex) {
            publishRetryTopic(topicsConfig.getRetryTopic().getName(), consumerRecord, 1);
            ack.acknowledge();
            log.error("ack exception while processing record: {}", getRecordInfo(consumerRecord), ex);
        }
    }

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = {"${topics.retryTopic.name}"}, id = "orders-retry-consumer")
    public void processRetry(Acknowledgment ack, ConsumerRecord<String, byte[]> consumerRecord) {
        try {
            OutboxOrder outboxOrder = serializerUtil.deserialize(consumerRecord.value(), OutboxOrder.class);
            ack.acknowledge();

            log.info("CONSUMER RETRY RECORD: {}", getRecordInfo(consumerRecord));
        } catch (Exception ex) {

            int currentRetry = Integer.parseInt(Arrays.toString(consumerRecord.headers().lastHeader(RETRY_COUNT_HEADER).value()));
            if (currentRetry > MAX_RETRY_COUNT) {
                publishRetryTopic(topicsConfig.getDeadLetterQueue().getName(), consumerRecord, currentRetry + 1);
                ack.acknowledge();
                log.error("MAX_RETRY_COUNT exceed, send record to DLQ: {}", getRecordInfo(consumerRecord));
            }

            publishRetryTopic(topicsConfig.getRetryTopic().getName(), consumerRecord, currentRetry + 1);
            ack.acknowledge();
            log.error("exception while processing: {}, record: {}", ex.getLocalizedMessage(), getRecordInfo(consumerRecord));

        }
    }

    private void publishRetryTopic(String topic, ConsumerRecord<String, byte[]> record, Integer retryCount) {
        record.headers().remove(RETRY_COUNT_HEADER);
        record.headers().add(RETRY_COUNT_HEADER, retryCount.toString().getBytes());
        publisher.publish(topic, record.key(), record.value());

        log.info("publishing retry record: {}, retryCount: {}", record.value(), retryCount);
    }

    private String getRecordInfo(ConsumerRecord<String, byte[]> record) {
        return "Topic: " + record.topic() + ", Key: " + record.key() + ", Partition: " + record.partition() + ", Offset: " + record.offset() + ", Timestamp: " + record.timestamp() + ", Value: " + Arrays.toString(record.value()) + ", Headers: " + record.headers() + " ";
    }
}
