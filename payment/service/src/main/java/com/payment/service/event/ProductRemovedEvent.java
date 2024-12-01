package com.payment.service.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRemovedEvent extends BaseEvent {
    public static final String EVENT = "PRODUCT_ITEM_REMOVED";
    private Long orderId;
    private Long productItemId;

    public ProductRemovedEvent(Long orderId, Long productItemId) {
        super(orderId);
        this.orderId = orderId;
        this.productItemId = productItemId;
    }
}
