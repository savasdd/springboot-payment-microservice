package com.payment.service.publisher;

import com.payment.common.enums.EventType;
import com.payment.common.utils.SerializerUtil;
import com.payment.entity.content.KafkaContent;
import com.payment.service.event.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotifySerializer {
    private final SerializerUtil serializerUtil;

    public KafkaContent notification(Long id, String userId, String message, EventType eventType) {
        return generateContent(id, new NotificationEvent(id, userId, message), eventType);
    }


    private KafkaContent generateContent(Long aggregateId, Object data, EventType eventType) {
        return KafkaContent.builder().aggregateId(aggregateId).eventType(eventType.getName()).data(serializerUtil.serializeToBytes(data)).build();
    }
}
