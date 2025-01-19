package com.payment.service.publisher;

import com.payment.common.enums.EventType;
import com.payment.common.utils.SerializerUtil;
import com.payment.entity.model.Order;
import com.payment.entity.model.OutboxOrder;
import com.payment.entity.model.ProductItem;
import com.payment.service.event.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OutboxSerializer {
    private final SerializerUtil serializerUtil;

    public OutboxOrder event(Order order, EventType eventType) {
        return generateOutboxOrder(order.getId(), new PaymentEvent(order.getId(), null, null, null), eventType);
    }

    public OutboxOrder event(Order order, ProductItem item, EventType eventType) {
        return generateOutboxOrder(order.getId(), new PaymentEvent(order.getId(), null, item, null), eventType);
    }

    public OutboxOrder event(Order order, String paymentId, String description, EventType eventType) {
        return generateOutboxOrder(order.getId(), new PaymentEvent(order.getId(), paymentId, null, description), eventType);
    }

    private OutboxOrder generateOutboxOrder(Long aggregateId, Object data, EventType eventType) {
        return OutboxOrder.builder().aggregateId(aggregateId).eventType(eventType.getName()).data(serializerUtil.serializeToBytes(data)).build();
    }
}
