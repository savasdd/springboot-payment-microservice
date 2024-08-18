package com.payment.service.event;

import com.payment.entity.model.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedEvent extends BaseEvent {
    private static final String ORDER_CREATED_EVENT = "ORDER_CREATED";

    CreatedEvent(Order order) {
        super(order.getId().toString());
    }
}
