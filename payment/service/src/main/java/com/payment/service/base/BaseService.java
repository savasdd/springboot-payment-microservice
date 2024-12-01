package com.payment.service.base;

import com.payment.common.config.KafkaTopicsConfig;
import com.payment.entity.content.KafkaContent;
import com.payment.entity.model.OutboxOrder;
import com.payment.repository.OutboxOrderRepository;
import com.payment.service.publisher.NotifySerializer;
import com.payment.service.publisher.Publisher;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@EqualsAndHashCode(callSuper = false)
public class BaseService implements Serializable {
    private final OutboxOrderRepository outboxRepository;
    private final Publisher publisher;
    private final NotifySerializer notifySerializer;
    private final KafkaTopicsConfig topicsConfig;

    public BaseService(OutboxOrderRepository outboxRepository, Publisher publisher, NotifySerializer notifySerializer, KafkaTopicsConfig topicsConfig) {
        this.outboxRepository = outboxRepository;
        this.publisher = publisher;
        this.notifySerializer = notifySerializer;
        this.topicsConfig = topicsConfig;
    }

    public String generateOrderNo() {
        int min = 10000;
        int max = 90000;

        Set<Integer> set = new Random().ints(min, max - min + 1).distinct().limit(6).boxed().collect(Collectors.toSet());
        return set.stream().findFirst().get() + String.valueOf(LocalDate.now().getYear());
    }

    public String generatePaymentNo() {
        int min = 100000;
        int max = 900000;

        Set<Integer> set = new Random().ints(min, max - min + 1).distinct().limit(6).boxed().collect(Collectors.toSet());
        return "000" + set.stream().findFirst().get();
    }

    public void publishOutbox(OutboxOrder event) {
        try {
            OutboxOrder outboxOrder = outboxRepository.save(event);
            log.info("publishing outbox event: {}", outboxOrder);
            outboxRepository.deleteById(outboxOrder.getId());
            publisher.publish(topicsConfig.getTopicName(outboxOrder.getEventType()), String.valueOf(outboxOrder.getAggregateId()), outboxOrder);

            log.info("outbox event published and deleted: {}", outboxOrder.getId());
        } catch (Exception e) {
            log.error("exception while publishing outbox event: {}", e.getLocalizedMessage());
        }
    }

    public void sendNotification(Long userId, String message) {
        try {
            KafkaContent event = notifySerializer.notification(generateNotifyNo(), String.valueOf(userId), message);
            log.info("publishing notification event: {}", event);
            publisher.publish(topicsConfig.getTopicName(event.getEventType()), String.valueOf(event.getAggregateId()), event);

            log.info("notification event published: {}", event.getAggregateId());
        } catch (Exception e) {
            log.error("exception while publishing notification    event: {}", e.getLocalizedMessage());
        }
    }

    private Long generateNotifyNo() {
        int min = 100;
        int max = 900;

        Set<Integer> set = new Random().ints(min, max - min + 1).distinct().limit(6).boxed().collect(Collectors.toSet());
        return Long.valueOf(set.stream().findFirst().get() + String.valueOf(new Date().getTime()));
    }


}
