package com.payment.service.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmittedEvent extends BaseEvent {
    private static final String ORDER_SUBMITTED_EVENT = "ORDER_SUBMITTED";
    private String orderId;

    SubmittedEvent(String orderId) {
        super(orderId);
    }
}
