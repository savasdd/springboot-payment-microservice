package com.payment.service.base;

import com.payment.common.config.KafkaTopicsConfig;
import com.payment.common.enums.EventType;
import com.payment.common.utils.ConstantUtil;
import com.payment.entity.content.KafkaContent;
import com.payment.entity.model.Order;
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
    private static final String RETRY_COUNT_HEADER = "retryCount";
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
            publisher.publish(topicsConfig.getTopicName(EventType.fromValue(event.getEventType())), String.valueOf(outboxOrder.getAggregateId()), outboxOrder);

            log.info("outbox event published and deleted: {}", outboxOrder.getId());
        } catch (Exception e) {
            log.error("exception while publishing outbox event: {}", e.getLocalizedMessage());
        }
    }

    public void publishOutboxNotification(OutboxOrder event, Order order) {
        try {
            EventType eventType = EventType.fromValue(event.getEventType());
            OutboxOrder outboxOrder = outboxRepository.save(event);
            log.info("publishing outbox event: {}", outboxOrder);
            outboxRepository.deleteById(outboxOrder.getId());
            publisher.publish(topicsConfig.getTopicName(eventType), String.valueOf(outboxOrder.getAggregateId()), outboxOrder);
            log.info("outbox event published and deleted: {}", outboxOrder.getId());

            sendNotification(order.getUserId(), eventType.getMessage() + " - " + order.getOrderNo(), EventType.NOTIFICATION);
        } catch (Exception e) {
            log.error("exception while publishing outbox event: {}", e.getLocalizedMessage());
        }
    }

    public void sendNotification(Long userId, String message, EventType eventType) {
        try {
            KafkaContent event = notifySerializer.notification(generateNotifyNo(), String.valueOf(userId), message, eventType);
            log.info("publishing notification event: {}", event);
            publisher.publish(topicsConfig.getTopicName(eventType), String.valueOf(event.getAggregateId()), event, Map.of(RETRY_COUNT_HEADER, "1".getBytes()));

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
