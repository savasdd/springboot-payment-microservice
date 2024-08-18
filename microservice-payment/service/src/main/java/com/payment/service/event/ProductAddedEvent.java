package com.payment.service.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductAddedEvent extends BaseEvent {
    private static final String PRODUCT_ITEM_REMOVED_EVENT = "PRODUCT_ITEM_REMOVED";
    private String orderId;
    private String productItemId;

    ProductAddedEvent(String orderId, String productItemId) {
        super(orderId);
        this.productItemId = productItemId;
    }
}
