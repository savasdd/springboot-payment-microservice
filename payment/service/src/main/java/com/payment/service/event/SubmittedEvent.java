package com.payment.service.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmittedEvent extends BaseEvent {
    public static final String EVENT = "ORDER_SUBMITTED";
    private Long orderId;

    public SubmittedEvent(Long orderId) {
        super(orderId);
        this.orderId = orderId;
    }
}
