package com.payment.service.event;

import com.payment.entity.model.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedEvent extends BaseEvent {
    public static final String EVENT = "ORDER_CREATED";
    private Order order;

    public CreatedEvent(Order order) {
        super(order.getId());
        this.order = order;
    }
}
