package com.payment.service.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentEvent extends BaseEvent {
    public static final String EVENT = "ORDER_PAID";
    private Long orderId;
    private String paymentId;

    public PaymentEvent(Long orderId, String paymentId) {
        super(orderId);
        this.orderId = orderId;
        this.paymentId = paymentId;
    }
}
