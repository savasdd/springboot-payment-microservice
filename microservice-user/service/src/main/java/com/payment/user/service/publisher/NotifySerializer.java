package com.payment.user.service.publisher;

import com.payment.user.common.utils.SerializerUtil;
import com.payment.user.entity.content.KafkaContent;
import com.payment.user.entity.event.NotificationEvent;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotifySerializer {
    private final SerializerUtil serializerUtil;

    public KafkaContent notification(String id, String userId, String message) {
        return generateContent(id, new NotificationEvent(id, userId, message), NotificationEvent.EVENT);
    }


    private KafkaContent generateContent(String aggregateId, Object data, String eventType) {
        return KafkaContent.builder().aggregateId(aggregateId).eventType(eventType).data(serializerUtil.serializeToBytes(data)).build();
    }
}