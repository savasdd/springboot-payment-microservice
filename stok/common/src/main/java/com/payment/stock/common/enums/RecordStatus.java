package com.payment.stock.common.enums;

import lombok.Getter;

@Getter
public enum RecordStatus {
    ACTIVE(0),
    DELETED(1);

    private final Integer code;

    RecordStatus(int code) {
        this.code = code;
    }
}
