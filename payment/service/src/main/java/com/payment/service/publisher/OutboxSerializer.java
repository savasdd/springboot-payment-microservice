package com.payment.service.publisher;

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

    public OutboxOrder createEvent(Order order) {
        return generateOutboxOrder(order.getId(), new CreatedEvent(order), CreatedEvent.EVENT);
    }

    public OutboxOrder cancelledEvent(Order order, String description) {
        return generateOutboxOrder(order.getId(), new CancelledEvent(order.getId(), description), CancelledEvent.EVENT);
    }

    public OutboxOrder completedEvent(Order order) {
        return generateOutboxOrder(order.getId(), new CompletedEvent(order.getId()), CompletedEvent.EVENT);
    }

    public OutboxOrder paidEvent(Order order, String paymentId) {
        return generateOutboxOrder(order.getId(), new PaymentEvent(order.getId(), paymentId), PaymentEvent.EVENT);
    }

    public OutboxOrder productAddedEvent(Order order, ProductItem productItem) {
        return generateOutboxOrder(order.getId(), new ProductAddedEvent(order.getId(), productItem.getId()), ProductAddedEvent.EVENT);
    }

    public OutboxOrder productRemovedEvent(Order order, Long productItemId) {
        return generateOutboxOrder(order.getId(), new ProductRemovedEvent(order.getId(), productItemId), ProductRemovedEvent.EVENT);
    }

    public OutboxOrder submittedEvent(Order order) {
        return generateOutboxOrder(order.getId(), new SubmittedEvent(order.getId()), SubmittedEvent.EVENT);
    }

    private OutboxOrder generateOutboxOrder(Long aggregateId, Object data, String eventType) {
        return OutboxOrder.builder().aggregateId(aggregateId).eventType(eventType).data(serializerUtil.serializeToBytes(data)).build();
    }
}
