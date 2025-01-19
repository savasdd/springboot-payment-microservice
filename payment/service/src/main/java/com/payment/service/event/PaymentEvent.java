package com.payment.service.event;

import com.payment.entity.model.ProductItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentEvent extends BaseEvent {
    private Long orderId;
    private String paymentId;
    private ProductItem item;
    private String description;

    public PaymentEvent(Long orderId, String paymentId, ProductItem item, String description) {
        super(orderId);
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.item = item;
        this.description = description;
    }

}
