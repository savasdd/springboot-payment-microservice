package com.payment.service.publisher;

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

    public KafkaContent notification(Long id, String userId, String message) {
        return generateContent(id, new NotificationEvent(id, userId, message), NotificationEvent.EVENT);
    }


    private KafkaContent generateContent(Long aggregateId, Object data, String eventType) {
        return KafkaContent.builder().aggregateId(aggregateId).eventType(eventType).data(serializerUtil.serializeToBytes(data)).build();
    }
}
