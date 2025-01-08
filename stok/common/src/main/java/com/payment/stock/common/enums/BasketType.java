package com.payment.stock.common.enums;

import lombok.Getter;

@Getter
public enum BasketType {
    NEW(0),
    BUY(1);

    private final Integer code;

    BasketType(int code) {
        this.code = code;
    }
}
